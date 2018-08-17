JAVAC = javac
JVM = java
JAVADOC = javadoc
MKBIN = mkdir -p bin

JAVAC_FLAGS = -g -d 'bin/'
JAVAC_CP = -cp

MAINSRC = src/main/
TESTSRC = src/test/
LIB = lib/jedis-2.9.0.jar:lib/junit-4.10.jar:lib/hamcrest-core-1.3.jar:lib/commons-pool2-2.6.0.jar::.

PKGSRC = /
TARGET = bin

classes = bin/*

MAIN = Main

MAINTEST = MainTest

.SUFFIXES : .class .java

all: 
	$(MKBIN)
	$(JAVAC) $(JAVAC_FLAGS) $(JAVAC_CP) $(LIB) $(MAINSRC)$(PKGSRC)*
	jar cfe bin/LRUCache.jar Main -C bin/ .

test:
	$(JAVAC) $(JAVAC_FLAGS) $(JAVAC_CP) bin/:$(LIB) $(TESTSRC)$(PKGSRC)*

clean:
	rm -rf $(TARGET)

run:
	$(JVM) $(JAVAC_CP) $(TARGET):$(LIB) $(MAIN)

run_test: 
	$(JVM) $(JAVAC_CP) $(TARGET):$(LIB) $(MAINTEST)

.PHONY: all test clean run run_test
