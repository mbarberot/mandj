# Chemins utilises a la compilation
RACINE=../../
BIN=$(RACINE)bin/
LIBDIR=$(RACINE)lib/
INC=$(RACINE)include/
SRCDIR=./

# Chemins :
# - vers la JDK Java
# - vers la JVM
JDK_HOME=/usr/lib/jvm/java-6-openjdk/
JDK_JVM=$(JDK_HOME)jre/lib/i386/client/

# Commandes de compilation
CC=gcc
CFLAGS=-O4 -W -Wall -g
CPPFLAGS=-I$(INC) -I$(JDK_HOME)include -I$(JDK_HOME)include/linux
LDFLAGS=-L $(LIBDIR) -L$(JDK_JVM) -lsocket -lpthread -ljvm 
EXEC = client

# Sources necessaires
SRC= $(wildcard $(SRCDIR)*.c) 
OBJ= $(SRC:.c=.o)
HDR= $(SRC:.c=.h)

# Règles
all:$(EXEC)

$(EXEC): $(OBJ)
	$(CC) -o $(BIN)$@ $^ $(CFLAGS) $(CPPFLAGS) $(LDFLAGS) 

%.o: %.c %.h
	$(CC) -o $@ -c $< $(CFLAGS) $(CPPFLAGS) $(LDFLAGS)

clean:
	rm $(SRCDIR)*.o

rmtemp:
	rm $(SRCDIR)*~
	rm *~

reset:
	rm $(BIN)$(EXEC) 

veryclean: clean reset

