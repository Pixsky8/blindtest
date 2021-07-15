#include "config.hh"

#define HOST "http://localhost"

#if defined(__linux__)
Config g_config(HOST, "/tmp/cookie.txt");
#elif defined(__APPLE__)
Config g_config(HOST, "/Volumes/RamDisk/cookie.txt");
#else
Config g_config(HOST, "cookie.txt");
#endif
