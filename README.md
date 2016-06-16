# umfpack-interface

# Requirements

sudo apt-get install libsuitesparse-metis-3.1.0 libsuitesparse-dev
# is actually part of the latest ubuntu release - not required
# but did need to do:
cd /usr/lib
sudo ln -s libumfpack.so.3.1.0 libumfpack.so

# Note:
recent versions of BridJ should fix this...
# see http://unix.stackexchange.com/questions/475/how-do-so-shared-object-numbers-work
# https://groups.google.com/forum/#!topic/nativelibs4java/3UTRv6q8Qls
