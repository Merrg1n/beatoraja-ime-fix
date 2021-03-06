#include "merrg1n_jajaime_IMEManager.h"
#include "keymap.h"
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

JNIEXPORT jboolean JNICALL Java_merrg1n_jajaime_IMEManager_isKeyPressed(JNIEnv* env, jclass clazz, jint keycode)
{
	if (keycode < 0 && keycode > 255)
		return false;
	int vkcode = keymap[keycode];
	if (vkcode)
		return GetAsyncKeyState(vkcode) & 0x8000;
	else
		return false;
}