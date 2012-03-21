#ifndef _fctSocket_
#define _fctSocket_

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <errno.h>
#include <unistd.h>

int socketServeur(ushort port);
int socketClient(char* nomMachine, ushort port);
int socketUDP(ushort port);
int adresseUDP(char* nomMachine, ushort port, struct sockaddr_in* addr);
void erreurSock(char* message, int sock);

#endif

