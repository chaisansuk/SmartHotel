#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include "CNoiseReduction.h"
#include <jni.h>
#include <android/log.h>

static CNoiseReduction *s_obj = 0;

#ifdef __cplusplus
extern "C" {
#endif
CNoiseReduction::CNoiseReduction() {
}

CNoiseReduction::~CNoiseReduction() {
	ReducDestroy();
}

void CNoiseReduction::Reduct(short sIn[], short sOut[], int nLen) {
	int i;
	int nbBytes;
	static bool bInit = false;

	short in[FRAME_SIZE];
	short out[FRAME_SIZE];
	float input[FRAME_SIZE];
	float output[FRAME_SIZE];
	char cbits[200];

	if (!bInit) {
		ReducInit();
		bInit = true;
	}

	int nRedPos = 0;
	while (1) {
		memset(out, 0, FRAME_SIZE * sizeof(short));
		//读入一帧16bits的声音    
		if (nRedPos + FRAME_SIZE > nLen) {
			memcpy(sOut + nRedPos, sIn + nRedPos,
					(nLen - nRedPos) * sizeof(short));
			break;
		} else {
			memcpy(in, sIn + nRedPos, FRAME_SIZE * sizeof(short));
		}

		//把16bits的值转化为float,以便speex库可以在上面工作    
		spx_int16_t * ptr = (spx_int16_t *) in;

		speex_preprocess_run(m_st, ptr);
		for (i = 0; i < FRAME_SIZE; i++)

			input[i] = in[i];

		//清空这个结构体里所有的字节,以便我们可以编码一个新的帧    

		speex_bits_reset(&bitsEncode);

		//对帧进行编码    

		speex_encode(stateEncode, input, &bitsEncode);
		//把bits拷贝到一个利用写出的char型数组    
		nbBytes = speex_bits_write(&bitsEncode, cbits, 200);

		//清空这个结构体里所有的字节,以便我们可以编码一个新的帧    
		speex_bits_reset(&bitsDecode);
		//将编码数据如读入bits    
		speex_bits_read_from(&bitsDecode, cbits, nbBytes);
		//对帧进行解码    
		speex_decode(stateDecode, &bitsDecode, output);
		for (i = 0; i < FRAME_SIZE; i++)
			out[i] = output[i];
		memcpy(sOut + nRedPos, out, FRAME_SIZE * sizeof(short));
		nRedPos += FRAME_SIZE;
	}
}

void CNoiseReduction::ReducInit() {
	int iQuality = 8;
	float fVbrQuality;
	int iAgc;
	int iVad;
	int iDenoise;
	int iNoiseSuppress;
	int iVadProbStart;
	int iVadProbContinue;

	//新建一个新的编码状态在窄宽(narrowband)模式下    

	stateEncode = speex_encoder_init(&speex_nb_mode);
	stateDecode = speex_decoder_init(&speex_nb_mode);
	fVbrQuality = 8;
	//设置质量为8(15kbps)    

//	tmp = 0;
//	speex_encoder_ctl(stateEncode, SPEEX_SET_VBR, &tmp);    
	speex_encoder_ctl(stateEncode, SPEEX_SET_VBR_QUALITY, &fVbrQuality);
	iQuality = 8;
	speex_encoder_ctl(stateEncode, SPEEX_SET_QUALITY, &iQuality);

	//初始化结构使他们保存数据    

	speex_bits_init(&bitsEncode);
	speex_bits_init(&bitsDecode);

	m_st = speex_preprocess_state_init(160, 8000);

	//  echo_state = speex_echo_state_init(160, 8000);     
	iDenoise = 1;
	iNoiseSuppress = -25;
	speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_DENOISE, &iDenoise); //降噪    
	speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_NOISE_SUPPRESS,
			&iNoiseSuppress); //设置噪声的dB

	iAgc = 0;
	fVbrQuality = 1200;
	//actually default is 8000(0,32768),here make it louder for voice is not loudy enough by default. 8000    
	speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_AGC, &iAgc); //增益
	speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_AGC_LEVEL, &fVbrQuality);

	iVad = 1;
	iVadProbStart = 80;
	iVadProbContinue = 65;
	speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_VAD, &iVad); //静音检测    
	speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_PROB_START, &iVadProbStart); //Set probability required for the VAD to go from silence to voice
	speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_PROB_CONTINUE,
			&iVadProbContinue); //Set probability required for the VAD to stay in the voice state (integer percent)
}

void CNoiseReduction::ReducDestroy() {

	//释放编码器状态量    

	speex_encoder_destroy(stateEncode);

	//释放bit_packing结构    

	speex_bits_destroy(&bitsEncode);
	speex_decoder_destroy(stateDecode);

	//释放bit_packing结构    

	speex_bits_destroy(&bitsDecode);
}

JNIEXPORT jint JNICALL Java_ItonLifecubeJni_NoiseReduction_init(JNIEnv *env,
		jobject obj) {
	s_obj = new CNoiseReduction();
	__android_log_print(ANDROID_LOG_DEBUG, "jni_log_init", "hello noise......");
	return 1;
}
JNIEXPORT jint JNICALL Java_ItonLifecubeJni_NoiseReduction_Reduct(JNIEnv *env,
		jobject obj, jshortArray In, jshortArray Out, jint len) {
	jshort temp_buffer[FRAME_SIZE];
	jshort temp_out[FRAME_SIZE];
	jsize temp = len;

	env->GetShortArrayRegion(In, 0, FRAME_SIZE, temp_buffer);
	s_obj->Reduct(temp_buffer, temp_out, temp);
	env->SetShortArrayRegion(Out, 0, FRAME_SIZE, temp_out);
	__android_log_print(ANDROID_LOG_DEBUG, "jni_log_TEMP", "temp:%d", temp);
	__android_log_print(ANDROID_LOG_DEBUG, "jni_log_IN", "\r\n......In......");
	for (int i = 0; i < temp; i++) {
		__android_log_print(ANDROID_LOG_DEBUG, "jni_log_IN", "%x",
				temp_buffer[i]);
	}
	__android_log_print(ANDROID_LOG_DEBUG, "jni_log_OUT",
			"\r\n......Out......");
	for (int j = 0; j < temp; j++) {
		__android_log_print(ANDROID_LOG_DEBUG, "jni_log_OUT", "%x",
				temp_out[j]);
	}
	return 2;
}

JNIEXPORT int JNICALL Java_ItonLifecubeJni_NoiseReduction_DenoiseClose(
		JNIEnv *env, jobject obj) {
	s_obj->ReducDestroy();
	delete s_obj;
	return 3;
}

#ifdef __cplusplus
}
#endif
