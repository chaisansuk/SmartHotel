#include <string.h>
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include "memory.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     itonlifecubeni_My3DesAlgorithm
 * Method:    Encrypt
 * Signature: wtbee
 */
/*********************����*********************/
JNIEXPORT jint JNICALL Java_itonlifecubeni_My3DesAlgorithm_Encrypt(JNIEnv *env,
		jclass thiz, jbyteArray msg, jbyteArray key, jbyteArray cipher,
		jint length) {
	jbyte * pMsg = (jbyte*) (*env)->GetByteArrayElements(env, msg, 0);
	jbyte * pKey = (jbyte*) (*env)->GetByteArrayElements(env, key, 0);
	jbyte *pCipher = (jbyte*) (*env)->GetByteArrayElements(env, cipher, 0);

	if (!pMsg || !pKey || !pCipher) {
		return -1;
	}
	int flag = Encrypt(pMsg, pKey, pCipher, length); //���ܺ���
	(*env)->ReleaseByteArrayElements(env, msg, pMsg, 0);
	(*env)->ReleaseByteArrayElements(env, key, pKey, 0);
	(*env)->ReleaseByteArrayElements(env, cipher, pCipher, 0);
	return flag;
}
/*********************����*********************/
JNIEXPORT jint JNICALL Java_itonlifecubeni_My3DesAlgorithm_Decrypt(JNIEnv *env,
		jclass thiz, jbyteArray cipher, jbyteArray key, jbyteArray result,
		jint length) {
	jbyte * pCipher = (jbyte*) (*env)->GetByteArrayElements(env, cipher, 0);
	jbyte * pKey = (jbyte*) (*env)->GetByteArrayElements(env, key, 0);
	jbyte *pResult = (jbyte*) (*env)->GetByteArrayElements(env, result, 0);

	if (!pResult || !pKey || !pCipher) {
		return -1;
	}
	int flag = Decrypt(pCipher, pKey, pResult, length); //���ܺ���
	(*env)->ReleaseByteArrayElements(env, result, pResult, 0);
	(*env)->ReleaseByteArrayElements(env, key, pKey, 0);
	(*env)->ReleaseByteArrayElements(env, cipher, pCipher, 0);
	return flag;
}

#ifdef __cplusplus
}
#endif

