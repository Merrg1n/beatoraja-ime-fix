#include "merrg1n_jajaime_IMEManager.h"
#include <Windows.h>
#include <imm.h>
#pragma comment (lib ,"imm32.lib") 

JNIEXPORT jlong JNICALL Java_merrg1n_jajaime_IMEManager_getHWND(JNIEnv* env, jclass clazz, jstring title)
{
	jboolean jb = true;
	const jchar* str = env->GetStringChars(title, &jb);
	HWND hwnd = FindWindow(NULL, (LPCWSTR)str);
	env->ReleaseStringChars(title, str);
	return (jlong)hwnd;
}

JNIEXPORT jlong JNICALL Java_merrg1n_jajaime_IMEManager_associateContext(JNIEnv* env, jclass clazz, jlong hwnd, jlong himc)
{
	return (jlong)ImmAssociateContext((HWND)hwnd, (HIMC)himc);
}

bool isKeyPressed(int keycode)
{
	if (keycode <= 0 || keycode > 255)
		return false;
	if (GetAsyncKeyState(keycode) & 0x8000)
		return true;
	return false;
}

JNIEXPORT jboolean JNICALL Java_merrg1n_jajaime_IMEManager_isKeyPressed(JNIEnv* env, jclass clazz, jint keycode)
{
	return isKeyPressed(keycode);
}

JNIEXPORT jboolean JNICALL JavaCritical_merrg1n_jajaime_IMEManager_isKeyPressed(jint keycode)
{
	return isKeyPressed(keycode);
}