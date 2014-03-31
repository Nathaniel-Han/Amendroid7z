#include<stdio.h>
#include<stdlib.h>
#include<android/log.h>
#include"com_amendroid7z_Main.h"
#include"7zip/UI/Console/MainAr.h"

static int chrcnt(const char *str,int letter)
{
	int count=0;
	while(*str)
	{
		if(*str == letter)
			count++;
		str++;
	}

	return count;
}

JNIEXPORT jint JNICALL Java_com_amendroid7z_Main_doeverything
  (JNIEnv *env, jobject thiz, jstring command)
{
	const char *str = env->GetStringUTFChars(command, NULL);

	    char *pch, *tmp, **myargv;

		int i, j, n;

		n = chrcnt(str,' ');

		myargv = (char**)calloc(n + 1, sizeof(char*));

		pch = strchr(str, ' ');

		i = pch - str + 1;

		myargv[0] = (char*)calloc(i, sizeof(char));

		strncpy(myargv[0], str, i - 1);
		myargv[0][i-1] = '\0';

		j = 0;

		j++;
		pch++;

		tmp = strchr(pch, ' ');
		while(tmp)
		{
			i = tmp - pch + 1;

			myargv[j] = (char*)calloc(i, sizeof(char));
		    strncpy(myargv[j], pch, i - 1);
		    myargv[j][i-1] = '\0';

			j ++;
		    pch = tmp + 1;
		    tmp = strchr(pch, ' ');
	  }

	  myargv[j] = (char*)calloc(strlen(pch) + 1 , sizeof(char));
	  strcpy(myargv[j], pch);

	  const char **c_myargv = (const char**)myargv;

	  interface(n + 1, c_myargv);

	  for(i = 0; i < n + 1; i++)
	     free(myargv[i]);
	  free(myargv);
}
