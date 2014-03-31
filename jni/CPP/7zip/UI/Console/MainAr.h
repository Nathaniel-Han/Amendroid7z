#ifndef MAINAR_H
#define MAINAR_H

#include "StdAfx.h"

#include "Common/NewHandler.h" // FIXME

#include "Common/MyException.h"
#include "Common/StdOutStream.h"

#include "Windows/Error.h"
#include "Windows/NtCheck.h"

#include "../Common/ArchiveCommandLine.h"
#include "../Common/ExitCode.h"

#include "ConsoleClose.h"

using namespace NWindows;

/*CStdOutStream *g_StdStream = 0;*/

int MY_CDECL interface(
#ifndef _WIN32
		int numArgs, const char *args[]
#endif
		);

#endif


