Amendroid7z
===========

This is an Android File Manager based on 7zip.

To compile 7zip into `Amendroid7z`, I utilized the NDK toolset provided by Google and made some modifications on the source code of 7zip.  Just follow instructions from official documentation, I wrote an NDK-style Makefile -- `Android.mk` under folder `jni/`. The output of NDK, dynamic link library -- `libmy7zip.so` is under folder `libs/armeabi/`. 

The main trick adopted here is to redirect the output of the program `7z`. Significant files(including modified source files from 7zip and the files I created for JNI) are shown as following:

<div>jni</div><div>├── Android.mk</div><div>├── CPP</div><div>│ &nbsp; ├── 7zip</div><div>│ &nbsp; │ &nbsp; └── UI</div><div>│ &nbsp; │ &nbsp; &nbsp; &nbsp; └── Console</div><div>│ &nbsp; │ &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ├── Main.cpp</div><div>│ &nbsp; │ &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ├── MainAr.cpp</div><div>│ &nbsp; │ &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; └── MainAr.h</div><div>│ &nbsp; ├── Common</div><div>│ &nbsp; │ &nbsp; ├── StdOutStream.cpp</div><div>│ &nbsp; │ &nbsp; └── StdOutStream.h</div><div>│ &nbsp; └── myWindows</div><div>│ &nbsp; &nbsp; &nbsp; └── config.h</div><div>├── com_nathaniel_amendroid7z_Main.cpp</div><div>└── com_nathaniel_amendroid7z_Main.h</div><div id="spnEditorSign">
</div>

For the sake of space, I didn't upload the whole source code of 7zip. Instead, I just give the file that I have modified (without the change of file path). 

I'm not the expert of Android NDK, but if you have any problem to compile 7zip using my Android.mk under NDK environment, please feel free to email me for discussion.