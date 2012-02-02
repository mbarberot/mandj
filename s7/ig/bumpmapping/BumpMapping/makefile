CPP=g++
CFLAGS=-Wall -lglut -lGLU -lGL
EXE=bumpexe
SRC=BumpMapping.cpp

all : $(EXE)

$(EXE) : $(SRC)
	$(CPP) $(SRC) $(CFLAGS) -o $(EXE)

clean : 
	rm $(EXE)