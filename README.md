Amendroid7z
===========

This is an Android File Manager based on 7zip.

To compile 7zip into `Amendroid7z`, I utilize the NDK toolset provided by Google. Just follow instructions from official documentation, I wrote an NDK-style Makefile -- `Android.mk` under folder `jni/`. The output of NDK, dynamic link library -- `libmy7zip.so` is under folder `libs/armeabi/`. 

For the sake of space, I didn't upload the whole source code of 7zip. Instead, I just give the file that I have modified (without the change of file path).

I'm not the expert of Android NDK, but if you have any problem to compile 7zip using my `Android.mk` under NDK environment, please feel free to email me for discussion.
