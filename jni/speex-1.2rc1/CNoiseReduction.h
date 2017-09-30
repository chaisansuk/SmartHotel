#ifndef __C_NOISE_REDUCTION_H__
#define __C_NOISE_REDUCTION_H__

#include <speex/speex.h>
#include <speex/speex_preprocess.h>
#include <speex/speex_echo.h>
#pragma comment(lib,"libspeexdsp.lib")

#define FRAME_SIZE 160


class CNoiseReduction
{
public:
	CNoiseReduction();
	~CNoiseReduction();

public:
	void Reduct(short sIn[], short sOut[], int nLen);
	void ReducDestroy();

private:
	void *stateEncode;    
	void *stateDecode;    

	SpeexBits bitsEncode;    
	SpeexBits bitsDecode;       

	SpeexPreprocessState * m_st;    

private:
	void ReducInit();
};





#endif
