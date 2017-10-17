JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	hashtagcounter.java \
	FibonacciHeap.java	\


default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
