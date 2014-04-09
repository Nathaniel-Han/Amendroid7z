// MainAr.cpp

#include"MainAr.h"


  CStdOutStream *g_StdStream = 0;

extern int _interface(
  #ifndef _WIN32
  int numArgs, const char *args[]
  #endif
);
/* hyx ============================================== */

static const char *kExceptionErrorMessage = "\n\nError:\n";
static const char *kUserBreak  = "\nBreak signaled\n";
static const char *kMemoryExceptionMessage = "\n\nERROR: Can't allocate required memory!\n";
static const char *kUnknownExceptionMessage = "\n\nUnknown Error\n";
static const char *kInternalExceptionMessage = "\n\nInternal Error #";

#define NT_CHECK_FAIL_ACTION (*g_StdStream) << "Unsupported Windows version"; return NExitCode::kFatalError;

/*int MY_CDECL main*/
/* hyx ============================================== */
int MY_CDECL interface
/* hyx ============================================== */
(
  #ifndef _WIN32
  int numArgs, const char *args[]
  #endif
)
{
  FILE *fp;
  if((fp = fopen("/data/data/com.nathaniel.amendroid7z/LOG","w")) == NULL)
	  return 3;


  CStdOutStream myStdStream(fp);
  g_StdStream = &myStdStream;

  NT_CHECK

  NConsoleClose::CCtrlHandlerSetter ctrlHandlerSetter;
  int res =  _interface(
    #ifndef _WIN32
    numArgs, args
    #endif
    );
  fclose(fp);
  return  res;
}
