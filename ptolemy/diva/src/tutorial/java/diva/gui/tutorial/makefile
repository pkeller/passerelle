# Makefile for gui tutorial
#
# @Version: $Id: makefile,v 1.3 2005/02/28 19:06:21 cxh Exp $
# @Authors: Christopher Hylands (makefile only)
#
# @Copyright (c) 2004-2005 The Regents of the University of California.
# All rights reserved.
#
# Permission is hereby granted, without written agreement and without
# license or royalty fees, to use, copy, modify, and distribute this
# software and its documentation for any purpose, provided that the
# above copyright notice and the following two paragraphs appear in all
# copies of this software.
#
# IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
# FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
# ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
# THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
# SUCH DAMAGE.
#
# THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
# INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
# PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
# CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
# ENHANCEMENTS, OR MODIFICATIONS.
#
# 						PT_COPYRIGHT_VERSION_2
# 						COPYRIGHTENDKEY

ME =		diva/gui/tutorial

DIRS =

# Root of the Ptolemy II directory
ROOT =		../../..

CLASSPATH =	$(ROOT)

# Get configuration info
CONFIG =	$(ROOT)/mk/ptII.mk
include $(CONFIG)

# Used to build jar files
PTPACKAGE = 	tutorial
PTCLASSJAR =	$(PTPACKAGE).jar

# Java files that implement tutorials/demos. 
DEMO_JSRCS = \
	AppletTutorial.java \
	ApplicationTutorial.java \
	MDIApplicationTutorial.java \

JSRCS = \
	AbstractApplication.java \
	MDIApplication.java \
	TextDocument.java \
	TextView.java \
	$(DEMO_JSRCS)

EXTRA_SRCS =	$(JSRCS) \
	AppletTutorial.htm 

# Sources that may or may not be present, but if they are present, we don't
# want make checkjunk to barf on them.
MISC_FILES =	$(DIRS)

# make checkjunk will not report OPTIONAL_FILES as trash
# make distclean removes OPTIONAL_FILES
OPTIONAL_FILES = \
	doc \
	$(PTCLASSJAR) \
	$(PTCLASSALLJAR) \
	'ApplicationTutorial$$1.class' \
	'ApplicationTutorial$$2.class' \
	'MDIApplication$$1.class' \
	'MDIApplication$$MDIViewListener.class' \
	'MDIApplicationTutorial$$1.class' \
	'TextDocument$$Factory.class' \
	'TextView$$1.class'

JCLASS = $(JSRCS:%.java=%.class)

all: jclass
	@echo "Run 'make demos' to run all the tutorials"

install: all jars

DEMOS = $(DEMO_JSRCS:%.java=%)
demos: $(DEMOS)

# Rules for running the examples. These are just there to save messing
# about with classpath
AppletTutorial:: jclass
	appletviewer AppletTutorial.htm

ApplicationTutorial:: jclass
	$(JAVA) -classpath $(CLASSPATH) diva.gui.tutorial.ApplicationTutorial

MDIApplicationTutorial:: jclass
	$(JAVA) -classpath $(CLASSPATH) diva.gui.tutorial.MDIApplicationTutorial
# Get the rest of the rules
include $(ROOT)/mk/ptcommon.mk
