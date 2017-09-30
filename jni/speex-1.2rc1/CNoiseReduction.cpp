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
		//����һ֡16bits������    
		if (nRedPos + FRAME_SIZE > nLen) {
			memcpy(sOut + nRedPos, sIn + nRedPos,
					(nLen - nRedPos) * sizeof(short));
			break;
		} else {
			memcpy(in, sIn + nRedPos, FRAME_SIZE * sizeof(short));
		}

		//��16bits��ֵת��Ϊfloat,�Ա�speex����������湤��    
		spx_int16_t * ptr = (spx_int16_t *) in;

		speex_preprocess_run(m_st, ptr);
		for (i = 0; i < FRAME_SIZE; i++)

			input[i] = in[i];

		//�������ṹ�������е��ֽ�,�Ա����ǿ��Ա���һ���µ�֡    

		speex_bits_reset(&bitsEncode);

		//��֡���б���    

		speex_encode(stateEncode, input, &bitsEncode);
		//��bits������һ������д����char������    
		nbBytes = speex_bits_write(&bitsEncode, cbits, 200);

		//�������ṹ�������е��ֽ�,�Ա����ǿ��Ա���һ���µ�֡    
		speex_bits_reset(&bitsDecode);
		//���������������bits    
		speex_bits_read_from(&bitsDecode, cbits, nbBytes);
		//��֡���н���    
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

	//�½�һ���µı���״̬��խ��(narrowband)ģʽ��    

	stateEncode = speex_encoder_init(&speex_nb_mode);
	stateDecode = speex_decoder_init(&speex_nb_mode);
	fVbrQuality = 8;
	//��������Ϊ8(15kbps)    

//	tmp = 0;
//	speex_encoder_ctl(stateEncode, SPEEX_SET_VBR, &tmp);    
	speex_encoder_ctl(stateEncode, SPEEX_SET_VBR_QUALITY, &fVbrQuality);
	iQuality = 8;
	speex_encoder_ctl(stateEncode, SPEEX_SET_QUALITY, &iQuality);

	//��ʼ���ṹʹ���Ǳ�������    

	speex_bits_init(&bitsEncode);
	speex_bits_init(&bitsDecode);

	m_st = speex_preprocess_state_init(160, 8000);

	//  echo_state = speex_echo_state_init(160, 8000);     
	iDenoise = 1;
	iNoiseSuppress = -25;
	speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_DENOISE, &iDenoise); //����    
	speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_NOISE_SUPPRESS,
			&iNoiseSuppress); //����������dB

	iAgc = 0;
	fVbrQuality = 1200;
	//actually default is 8000(0,32768),here make it louder for voice is not loudy enough by default. 8000    
	speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_AGC, &iAgc); //����
	speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_AGC_LEVEL, &fVbrQuality);

	iVad = 1;
	iVadProbStart = 80;
	iVadProbContinue = 65;
	speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_VAD, &iVad); //�������    
	speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_PROB_START, &iVadProbStart); //Set probability required for the VAD to go from silence to voice
	speex_preprocess_ctl(m_st, SPEEX_PREPROCESS_SET_PROB_CONTINUE,
			&iVadProbContinue); //Set probability required for the VAD to stay in the voice state (integer percent)
}

void CNoiseReduction::ReducDestroy() {

	//�ͷű�����״̬��    

	speex_encoder_destroy(stateEncode);

	//�ͷ�bit_packing�ṹ    

	speex_bits_destroy(&bitsEncode);
	speex_decoder_destroy(stateDecode);

	//�ͷ�bit_packing�ṹ    

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
