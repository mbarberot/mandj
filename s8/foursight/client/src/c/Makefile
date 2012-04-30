# Chemins utilises a la compilation
RACINE=../../
BIN=$(RACINE)bin/
LIBDIR=$(RACINE)lib/
INC=$(RACINE)include/
SRCDIR=./

# Commandes de compilation
CC=gcc
CFLAGS=-O4 -W -Wall -g
CPPFLAGS=-I $(INC) 
LDFLAGS=-L $(LIBDIR) -lsocket -lpthread
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

