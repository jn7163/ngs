# ===========================================================================
#
#                            PUBLIC DOMAIN NOTICE
#               National Center for Biotechnology Information
#
#  This software/database is a "United States Government Work" under the
#  terms of the United States Copyright Act.  It was written as part of
#  the author's official duties as a United States Government employee and
#  thus cannot be copyrighted.  This software/database is freely available
#  to the public for use. The National Library of Medicine and the U.S.
#  Government have not placed any restriction on its use or reproduction.
#
#  Although all reasonable efforts have been taken to ensure the accuracy
#  and reliability of the software and data, the NLM and the U.S.
#  Government do not and cannot warrant the performance or results that
#  may be obtained by using this software or data. The NLM and the U.S.
#  Government disclaim all warranties, express or implied, including
#  warranties of performance, merchantability or fitness for any particular
#  purpose.
#
#  Please cite the author in any work or product based on this material.
#
# ===========================================================================


default: std

TOP ?= $(CURDIR)
MODPATH =
include $(TOP)/Makefile.config

INTLIBS = \

# jar file containing all generated classes
EXTLIBS =       \
	ngs-java    \
	ngs-examples

TARGETS =      \
	$(INTLIBS) \
	$(EXTLIBS)

# if configure was able to locate where the JNI headers go
ifdef JNIPATH
TARGETS += ngs-jni
endif

all std: $(TARGETS)

clean:
	rm -rf $(LIBDIR)/ngs-java* $(CLSDIR)

.PHONY: default all std $(TARGETS)

#-------------------------------------------------------------------------------
# JAVA NGS
#
ngs-java: $(LIBDIR) $(CLSDIR) $(LIBDIR)/ngs-java.jar

# java API
NGS_SRC =                  \
	ErrorMsg               \
	Statistics             \
	Fragment               \
	FragmentIterator       \
	Read                   \
	ReadIterator           \
	ReadGroup              \
	ReadGroupIterator      \
	Alignment              \
	AlignmentIterator      \
	PileupEvent            \
	PileupEventIterator    \
	Pileup                 \
	PileupIterator         \
	Reference              \
	ReferenceIterator      \
	ReadCollection

NGS_SRC_PATH = \
	$(addprefix $(SRCDIR)/ngs/,$(addsuffix .java,$(NGS_SRC)))

$(CLSDIR)/ngs-java-api: $(NGS_SRC_PATH)
	$(JAVAC) $(DBG) $^ -d $(CLSDIR) $(CLSPATH) $(SRCINC) && touch $@

# java language bindings
ITF_SRC =                  \
	Refcount               \
	StatisticsItf          \
	FragmentItf            \
	FragmentIteratorItf    \
	ReadItf                \
	ReadIteratorItf        \
	ReadGroupItf           \
	ReadGroupIteratorItf   \
	AlignmentItf           \
	AlignmentIteratorItf   \
	PileupEventItf         \
	PileupEventIteratorItf \
	PileupItf              \
	PileupIteratorItf      \
	ReferenceItf           \
	ReferenceIteratorItf   \
	ReadCollectionItf

ITF_SRC_PATH = \
	$(addprefix $(SRCDIR)/ngs/itf/,$(addsuffix .java,$(ITF_SRC)))

$(CLSDIR)/ngs-java-itf: $(CLSDIR)/ngs-java-api $(ITF_SRC_PATH)
	$(JAVAC) $(DBG) $(ITF_SRC_PATH) -d $(CLSDIR) $(CLSPATH) $(SRCINC) && touch $@

# NCBI engine bindings
NCBI_SRC =                 \
	FileCreator            \
	Logger                 \
	HttpManager            \
	LibManager             \
	LibPathIterator        \
	Manager                \
	NGS

NCBI_SRC_PATH = \
	$(addprefix $(SRCDIR)/gov/nih/nlm/ncbi/ngs/,$(addsuffix .java,$(NCBI_SRC)))

$(CLSDIR)/ngs-java-ncbi: $(CLSDIR)/ngs-java-itf $(NCBI_SRC_PATH)
	$(JAVAC) $(DBG) $(NCBI_SRC_PATH) -d $(CLSDIR) $(CLSPATH) $(SRCINC) && touch $@

# rule to produce the jar
$(LIBDIR)/ngs-java.jar: $(CLSDIR)/ngs-java-api $(CLSDIR)/ngs-java-itf $(CLSDIR)/ngs-java-ncbi
	( cd $(CLSDIR); $(JAR) $@ `find . -name "*.class"` ) || ( rm -f $@ && false )


#-------------------------------------------------------------------------------
# NGS examples
#
ngs-examples: $(LIBDIR) $(CLSDIR) $(LIBDIR)/ngs-examples.jar

# java examples
NGS_EXAMPLES =    \
	RefTest       \
	FragTest      \
	AlignTest     \
	AlignSliceTest

NGS_EXAMPLES_PATH = \
	$(addprefix $(SRCDIR)/examples/,$(addsuffix .java,$(NGS_EXAMPLES)))

$(CLSDIR)/ngs-examples: $(NGS_EXAMPLES_PATH)
	$(JAVAC) $(DBG) $^ -d $(CLSDIR) $(CLSPATH) $(SRCINC) && touch $@

# rule to produce the jar
$(LIBDIR)/ngs-examples.jar: $(CLSDIR)/ngs-examples
	( cd $(CLSDIR); $(JAR) $@ `find examples -name "*.class"` ) || ( rm -f $@ && false )

#-------------------------------------------------------------------------------
# JNI headers
#
ifdef JNIPATH

ngs-jni: $(JNIPATH) $(JNIPATH)/headers-generated

JNI_ITF =                  \
	ReadCollectionItf      \
	ReadGroupItf           \
	ReadGroupIteratorItf   \
	ReferenceItf           \
	ReferenceIteratorItf   \
	PileupItf              \
	PileupIteratorItf      \
	PileupEventItf         \
	PileupEventIteratorItf \
	AlignmentItf           \
	AlignmentIteratorItf   \
	ReadItf                \
	ReadIteratorItf        \
	FragmentItf            \
	StatisticsItf          \
	Refcount

JNI_CLASSES =                        \
	$(addprefix ngs.itf.,$(JNI_ITF))

$(JNIPATH)/headers-generated: $(MAKEFILE) $(LIBDIR)/ngs-java.jar
	cd $(JNIPATH); $(JAVAH) -classpath $(CLSDIR) $(JNI_CLASSES)
	@ cd $(JNIPATH); echo 'for f in ngs_itf_*.h; do mv $$f jni_$${f#ngs_itf_}; done' | bash
	@ touch $@

endif