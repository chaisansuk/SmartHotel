#include <string.h>
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <android/log.h>
#include<netinet/in.h>

#include "ffmpeg/libavformat/avformat.h"
#include "ffmpeg/libavcodec/avcodec.h"
#include "ffmpeg/libswscale/swscale.h"
#include "g726.h"

////////////////////////////////
AVCodec *m_pCodec = NULL;
AVCodecContext *m_pCodecCtx = NULL;
AVFrame *m_pFrame = NULL;
struct SwsContext *pSwsCtx = NULL;
AVPacket m_packet;

int m_width = 0;
int m_height = 0;
const int MAX_VIDEO_W = 1280;
const int MAX_VIDEO_H = 720;
static int g_bInit = 0;
int g_iFrame = 0;

//picture
char *fill_buffer;
AVFrame *frame_rgb;
struct SwsContext *img_convert_ctx;
jboolean bPicture = JNI_FALSE;
///////////////////////////////
#define MAX_RGB_BUF 1280*720*3

// 海芯威视定义
#define PT_H264 96
#define PT_G726	97	
#define PT_G711 8
#define PT_DATA 100
// global
static int g_iConnState = 0;
int ret = -1;
int outSize = 0;

const int nVideoLen = 1280 * 720;
const int nBufLen = 512000;
char *g_pVideoData = NULL;
char *g_pBufData = NULL;
int g_nCopyLen = 0;
int g_nBufLen = 0;
int g_nFullPackLen = 0;
int g_nNeedLen = 0;
unsigned int g_ts = 0;
int g_tsLen = 0;
char *m_VideoBufData = NULL;
char *m_AudioBufData = NULL;
char *m_PCMData = NULL;
int g_curFrame = 0; //当前帧数
int g_curFrameSize = 0; //当前帧数大小

////////////////////////////////////
#define MAX_G726_BUF 320
#define MAX_PCM_SIZE 1024*5
#define G726_SIZE 320//解码后G726数据大小
g726_state_t *g_state = NULL; //for g726_xx.
int g726_index_write = 0;
int g726_index_read = 0;

//2. RTP数据包头格式：
typedef struct {
	/* byte 0 */
	unsigned short cc :4; /* CSRC count */
	unsigned short x :1; /* header extension flag */
	unsigned short p :1; /* padding flag */
	unsigned short version :2; /* protocol version */
	/* byte 1 */
	unsigned short pt :7; /* payload type */ //pt说明： 96=>H.264, 97=>G.726, 8=>G.711a, 100=>报警数据
	unsigned short marker :1; /* marker bit */
	/* bytes 2, 3 */
	unsigned short seqno :16; /* sequence number */
	/* bytes 4-7 */
	unsigned int ts; /* timestamp in ms */
	/* bytes 8-11 */
	unsigned int ssrc; /* synchronization source */
} RTP_HDR_S; // sizeof: 12

typedef struct {
	unsigned char daollar; /*8, $:dollar sign(24 decimal)*/
	unsigned char channelid; /*8, channel id*/
	unsigned short resv; /*16, reseved*/
	unsigned int payloadLen; /*32, payload length*/
	RTP_HDR_S rtpHead; /*rtp head*/
} RTSP_ITLEAVED_HDR_S; // sizeof: 20

typedef struct {
	unsigned char daollar; /*8, $:dollar sign(24 decimal)*/
	unsigned char channelid; /*8, channel id*/
	unsigned short resv; /*16, reseved*/
	unsigned int payloadLen; /*32, payload length*/
} RTSP_H_S; // sizeof: 8
///////////////////////////////////////////////////////////////

// 处理接收视频，音频等数据
int RecvPackData(const char *pBuf, int len) {

	const int N_RTP_HDR = sizeof(RTP_HDR_S);
	if (len < N_RTP_HDR) {
		return -1; //Error
	}
	RTP_HDR_S rtpHead;
	memcpy(&rtpHead, pBuf, N_RTP_HDR);
	unsigned int ts = ntohl(rtpHead.ts);

	//96=>H.264, 97=>G.726, 8=>G.711a, 100=>报警数据
	if (rtpHead.pt == PT_H264) {
		int nVideoLen = len - N_RTP_HDR;
		if (ts != g_ts && g_ts > 0) {
			int ret = DecodeH264(m_VideoBufData, g_tsLen); //满帧解码
			memset(m_VideoBufData, 0, MAX_RGB_BUF); //清理
			g_tsLen = 0;
			memcpy(&m_VideoBufData[g_tsLen], pBuf + N_RTP_HDR, nVideoLen); // 去除RTP_HDR头
			g_tsLen += nVideoLen;
			g_ts = ts;
			g_curFrame++;
			g_curFrameSize = g_tsLen;
			__android_log_print(ANDROID_LOG_DEBUG, "jni_log",
					"g_curFrameSize:%d --g_curFrame:%d", g_curFrameSize,
					g_curFrame);
		} else {
			memcpy(&m_VideoBufData[g_tsLen], pBuf + N_RTP_HDR, nVideoLen); // 去除RTP_HDR头
			g_tsLen += nVideoLen;
			g_ts = ts;
		}
	} else if (rtpHead.pt == PT_G726) {
		/****  G726 解码  *********/
		if (g726_index_write < MAX_PCM_SIZE) {
			int out_length = g726_decode(g_state,
					&m_PCMData[g726_index_write * G726_SIZE], m_AudioBufData,
					len - N_RTP_HDR - 4);

			if (out_length > 0) {
//				__android_log_print(ANDROID_LOG_DEBUG, "jni_log",
//						"G726 out_length:%d --g726_index_write:%d", out_length,
//						g726_index_write);
				g726_index_write++; //计数加1
			}
		} else {
			g726_index_write = 0;
		}
		memset(m_AudioBufData, 0, MAX_G726_BUF); //清理
		memcpy(m_AudioBufData, pBuf + N_RTP_HDR + 4, len - N_RTP_HDR - 4);
	}
	return 1;
}
// 解析通道
int ParseChannelData(const char *pBuf, int len) {
	const int N_H_S = sizeof(RTSP_H_S); // 8
	const int N_HDR_LEN = sizeof(RTSP_ITLEAVED_HDR_S); // 20
	memcpy(&g_pBufData[g_nBufLen], pBuf, len);
	g_nBufLen += len;

	if (g_nBufLen < N_HDR_LEN)
		return 0;

	if (g_nNeedLen == 0) {
		RTSP_ITLEAVED_HDR_S header;
		memset(&header, 0, N_HDR_LEN);
		memcpy(&header, g_pBufData, N_HDR_LEN);

		int packet_len = ntohl(header.payloadLen); // - sizeof(RTP_HDR_S);
		unsigned int timestamp = ntohl(header.rtpHead.ts);
		int streamType = header.rtpHead.pt;
		if (packet_len > 0 && header.daollar == 0x24) //"$"  // 验证
				{
			g_nFullPackLen = packet_len;

			// 当前包不够一个nalu包
			if (g_nBufLen < g_nFullPackLen + N_H_S) {
				int nCopy = g_nBufLen - N_H_S;
				memcpy(&g_pVideoData[0], &g_pBufData[N_H_S], nCopy);
				g_nCopyLen = nCopy;
				g_nBufLen = 0;
				if (g_nFullPackLen - nCopy > 0) {
					g_nNeedLen = g_nFullPackLen - nCopy;
				}
			} else // >= 单个nalu包
			{
				int nCopy = g_nFullPackLen;
				memcpy(&g_pVideoData[0], &g_pBufData[N_H_S], nCopy);
				g_nCopyLen = nCopy;
				g_nNeedLen = 0;
				//================================================
				//FULL PACK
				RecvPackData(g_pVideoData, nCopy);
				int nRemain = g_nBufLen - (g_nFullPackLen + N_H_S);
				g_nBufLen = 0;
				if (nRemain > 0) {
					char *pTemp = (char *) malloc(nRemain);
					memcpy(pTemp, &g_pBufData[g_nFullPackLen + N_H_S], nRemain);
					ParseChannelData(pTemp, nRemain);
					free(pTemp);
				}
			}
			return 0;
		} else {
			g_nCopyLen = 0;
			g_nBufLen = 0;
			g_nFullPackLen = 0;
			g_nNeedLen = 0;
			g_ts = 0;
			return 0;
		}
	}
	if (g_nNeedLen > 0) {
		if (g_nNeedLen > MAX_RGB_BUF) {
			g_nCopyLen = 0;
			g_nBufLen = 0;
			g_nFullPackLen = 0;
			g_nNeedLen = 0;
			return 0;
		}
		if (g_nBufLen < g_nNeedLen) {
			int nCopy = g_nBufLen;
			memcpy(&g_pVideoData[g_nCopyLen], g_pBufData, nCopy);
			g_nCopyLen += nCopy;
			g_nNeedLen -= nCopy;
			g_nBufLen = 0;
		} else {
			int nCopy = g_nNeedLen;
			memcpy(&g_pVideoData[g_nCopyLen], g_pBufData, nCopy);
			g_nCopyLen += nCopy;
			g_nNeedLen = 0;
			//================================================
			//FULL PACK
			RecvPackData(g_pVideoData, g_nFullPackLen);
			int nRemain = g_nBufLen - nCopy;
			g_nBufLen = 0;
			if (nRemain > 0) {
				char *pTemp = (char *) malloc(nRemain);
				memcpy(pTemp, &g_pBufData[nCopy], nRemain);
				ParseChannelData(pTemp, nRemain);
				free(pTemp);
			}
		}
	}
}
//====================================================
JNIEXPORT jint
JNICALL Java_ItonLifecubeJni_H264VideoWansview_init(JNIEnv* env, jobject thiz) {
	m_width = -1;
	m_height = -1;
	g_curFrame = 0;
	g_curFrameSize = 0;
	if (g_bInit == 1) {
		return -1;
	}
	if (g_pVideoData == NULL) {
		g_pVideoData = (char *) malloc(nVideoLen);
		g_pBufData = (char *) malloc(nBufLen);
		m_VideoBufData = (char *) malloc(MAX_RGB_BUF);
		m_AudioBufData = (char *) malloc(MAX_G726_BUF);
		m_PCMData = (char *) malloc(MAX_PCM_SIZE * G726_SIZE);
	}
	__android_log_print(ANDROID_LOG_DEBUG, "jni_log", " G726 init Start");
	//G726 init
	g_state = (g726_state_t *) malloc(sizeof(g726_state_t));
	g_state = g726_init(g_state, 8000 * 2);
	if (NULL == g_state) {
		return -1;
	}
	__android_log_print(ANDROID_LOG_DEBUG, "jni_log", " G726 init OK");
	//avcodec_init();
	av_register_all(); // Register all formats and codecs
	m_pCodec = avcodec_find_decoder(CODEC_ID_H264);
	if (!m_pCodec) {
		return -1;
	}
	m_pCodecCtx = avcodec_alloc_context3(m_pCodec);
	if (!m_pCodecCtx) {
		return -2;
	}
	if (avcodec_open2(m_pCodecCtx, m_pCodec, NULL) < 0)
		return -3;
	m_pFrame = avcodec_alloc_frame();
	if (!m_pFrame) {
		return -4;
	}
	av_init_packet(&m_packet);
	g_bInit = 1;
	g_iFrame = 0;

	return 1;
}

/*
 * Class:     h264_com_VView
 * Method:    UninitDecoder
 * Signature: ()I
 */

JNIEXPORT jint
JNICALL Java_ItonLifecubeJni_H264VideoWansview_finit(JNIEnv* env, jobject thiz) {
	if (g_bInit <= 0)
		return -1;

	if (g_pVideoData == NULL) {
		free(g_pVideoData);
		free(g_pBufData);
		free(m_VideoBufData);
		free(m_AudioBufData);
		free(m_PCMData);
		g_pVideoData = NULL;
		g_pBufData = NULL;
		m_VideoBufData = NULL;
		m_AudioBufData = NULL;
		m_PCMData = NULL;
		g_state = NULL;
	}
	// Close the codec
	if (m_pCodecCtx) {
		avcodec_close(m_pCodecCtx);
		m_pCodecCtx = NULL;
	}

	// Free the YUV frame
	if (m_pFrame != NULL) {
		av_free(m_pFrame);
		m_pFrame = NULL;
	}

	if (bPicture)
		FreePicture();
	m_width = -1;
	m_height = -1;
	g_bInit = 0;
	g726_index_write = 0;
	g726_index_read = 0;
	return 1;
}

/*
 * Class:     h264_com_VView
 * Method:    DecoderNal
 * Signature: ([B[I)I
 */
int InitPicture() {
	frame_rgb = avcodec_alloc_frame();
	if (!frame_rgb) {
		return -1;
	}
	int numBytes = avpicture_get_size(PIX_FMT_RGB565, m_width, m_height);
	fill_buffer = (char *) av_malloc(numBytes * sizeof(char));
	avpicture_fill((AVPicture *) frame_rgb, fill_buffer, PIX_FMT_RGB565,
			m_width, m_height);
	img_convert_ctx = sws_getContext(m_width, m_height, m_pCodecCtx->pix_fmt,
			m_width, m_height, PIX_FMT_RGB565, SWS_BICUBIC, NULL, NULL, NULL);
	bPicture = JNI_TRUE;
	return 1;
}
int FreePicture() {
	if (fill_buffer != NULL) {
		av_free(fill_buffer);
		fill_buffer = NULL;
	}
	if (frame_rgb != NULL) {
		av_free(frame_rgb);
		frame_rgb = NULL;
	}
	sws_freeContext(img_convert_ctx);
	bPicture = JNI_FALSE;
	return 1;
}
int DecodeH264(char *pByte, int nalLen) {
	if (g_bInit <= 0) {
		return -1;
	}
	int got_picture;
	int numBytes;
//	jbyte *pByte = (jbyte*)(*env)->GetByteArrayElements(env, in, 0);
	int nSrcLen = nalLen;
	m_packet.size = nSrcLen;
	m_packet.data = (unsigned char *) pByte;

	int consumed_bytes = avcodec_decode_video2(m_pCodecCtx, m_pFrame,
			&got_picture, &m_packet);
	if (consumed_bytes > 0) {
		if (m_pFrame->data[0]) {
			m_width = m_pCodecCtx->width;
			m_height = m_pCodecCtx->height;
//			__android_log_print(ANDROID_LOG_DEBUG, "jni_log", " w:%d---- h:%d",
//					m_width, m_height);
			g_curFrameSize = nSrcLen;
			g_curFrame++;
			__android_log_print(ANDROID_LOG_DEBUG, "jni_log",
					"g_curFrameSize:%d --g_curFrame:%d", g_curFrameSize,
					g_curFrame);

			if (!bPicture)
				InitPicture();
		}
	} else {
		m_width = -1;
		m_height = -1;
	}
//	(*env)->ReleaseByteArrayElements(env, in, pByte, 0);
	return 2;
}

JNIEXPORT jint
JNICALL Java_ItonLifecubeJni_H264VideoWansview_PackData(JNIEnv* env,
		jobject thiz, jbyteArray in, jint nalLen) {
	jbyte * Buf = (jbyte*) (*env)->GetByteArrayElements(env, in, 0);
	ParseChannelData(Buf, nalLen);
	(*env)->ReleaseByteArrayElements(env, in, Buf, 0);
}
JNIEXPORT jint
JNICALL Java_ItonLifecubeJni_H264VideoWansview_P2PPackData(JNIEnv* env,
		jobject thiz, jbyteArray in, jint nalLen) {
	jbyte * Buf = (jbyte*) (*env)->GetByteArrayElements(env, in, 0);
	int ret = -1;
	if (MAX_RGB_BUF >= nalLen) {
		memcpy(m_VideoBufData, Buf, nalLen);
		ret = DecodeH264(m_VideoBufData, nalLen);
		(*env)->ReleaseByteArrayElements(env, in, Buf, 0);
	}
	__android_log_print(ANDROID_LOG_DEBUG, "jni_log", "p2p:%d", ret);
	return ret;
}
JNIEXPORT jint
JNICALL Java_ItonLifecubeJni_H264VideoWansview_OutFrameData(JNIEnv* env,
		jobject thiz, jbyteArray out) {
	jbyte *pByte = (jbyte*) (*env)->GetByteArrayElements(env, out, 0);
	if (g_bInit <= 0) {
		return -1;
	}

	if (m_width <= 0 || m_height <= 0) {
//		__android_log_print(ANDROID_LOG_DEBUG, "jni_log",
//				"Err: jni::decode() w <0 or h < 0");
		return -1;
	}
	int ret = sws_scale(img_convert_ctx, m_pFrame->data, m_pFrame->linesize, 0,
			m_height, frame_rgb->data, frame_rgb->linesize);
	memcpy(pByte, frame_rgb->data[0], m_height * m_width * 2);

	if (pByte != NULL) {
		(*env)->ReleaseByteArrayElements(env, out, pByte, 0);
	}
	return 1;
}

JNIEXPORT jint
JNICALL Java_ItonLifecubeJni_H264VideoWansview_getFrame(JNIEnv* env,
		jobject thiz) {
	__android_log_print(ANDROID_LOG_DEBUG, "jni_log1", "g_curFrame:%d",
			g_curFrame);
	return g_curFrame;

}

JNIEXPORT jint
JNICALL Java_ItonLifecubeJni_H264VideoWansview_getSize(JNIEnv* env,
		jobject thiz) {
	__android_log_print(ANDROID_LOG_DEBUG, "jni_log1", "g_curFrameSize:%d",
			g_curFrameSize);
	return g_curFrameSize;

}

JNIEXPORT jint
JNICALL Java_ItonLifecubeJni_H264VideoWansview_getWidth(JNIEnv* env,
		jobject thiz) {
	if (m_width > 0)
		return m_width;
	return -1;
}

JNIEXPORT jint
JNICALL Java_ItonLifecubeJni_H264VideoWansview_getHeight(JNIEnv* env,
		jobject thiz) {
	if (m_height > 0)
		return m_height;
	return -1;
}

JNIEXPORT jint
JNICALL Java_ItonLifecubeJni_H264VideoWansview_getAudioData(JNIEnv* env,
		jobject thiz, jbyteArray out) {
	jbyte *pByte = (jbyte*) (*env)->GetByteArrayElements(env, out, 0);

	if (g726_index_read < g726_index_write && g726_index_read < MAX_PCM_SIZE) {
		memcpy(pByte, &m_PCMData[g726_index_read * G726_SIZE], G726_SIZE);
		g726_index_read++;
	} else {
		g726_index_read = 0;
	}
//	__android_log_print(ANDROID_LOG_DEBUG, "jni_log", "g726_index_read:%d",
//			g726_index_read);
	if (pByte != NULL) {
		(*env)->ReleaseByteArrayElements(env, out, pByte, 0);
	}
	return 1;
}

