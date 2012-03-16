# Chemins utilises a la compilation
BIN= bin/
LIBDIR=lib/
INC=include/
SRCDIR=src/

# Commandes de compilation
CC=gcc
CFLAGS=-O4 -W -Wall -g
CPPFLAGS=-I $(INC)
LDFLAGS=-L $(LIBDIR) -lsocket
EXEC = client

# Sources necessaires
SRC= $(wildcard $(SRCDIR)*.c) 
OBJ= $(SRC:.c=.o)
HDR= $(SRC:.c=.h)


all: $(EXEC) 

$(EXEC): $(OBJ)
	$(CC) -o $(BIN)$@ $^ $(CPPFLAGS) $(LDFLAGS)

%.o: %.c %.h
	$(CC) -o $@ -c $< $(CFLAGS) $(LDFLAGS) $(CPPFLAGS)

clean:
	rm *.o

reset:
	rm $(BIN)$(EXEC) 

veryclean: clean reset
