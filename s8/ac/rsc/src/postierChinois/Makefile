# Chemins utilises a la compilation
BIN= ../../bin/
LIBDIR=../../lib/
INC=../../include/

# Commandes de compilation
CC=gcc
CFLAGS=-O4 -W -Wall -g
CPPFLAGS=-I $(INC)
LDFLAGS=-L $(LIBDIR) -lgraphe -lliste
EXEC = postierChinois

# Sources necessaires
SRC= $(EXEC).c
OBJ= $(SRC:.c=.o)
HDR= $(SRC:.c=.h)

all: $(EXEC)

$(EXEC): $(OBJ)
	$(CC) -o $(BIN)$@ $^ $(LDFLAGS) $(CPPFLAGS) $(LDFLAGS)
	cp $(HDR) $(INC)

%.o: %.c %.h
	$(CC) -o $@ -c $< $(CFLAGS) $(LDFLAGS) $(CPPFLAGS)

clean:
	rm *.o

reset:
	rm $(INC)parser.h $(INC)menu.h

veryclean: clean reset
	
