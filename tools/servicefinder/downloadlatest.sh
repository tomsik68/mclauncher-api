#!/bin/sh
# Tomsik68's script to download the latest minecraft snapshot
echo "Downloading versions.json"
wget -q "https://s3.amazonaws.com/Minecraft.Download/versions/versions.json"
VERSION=`grep -m 1 -o -e '[0-9]*w[0-9]*[a-z]' versions.json`
FILE="$VERSION.jar"
echo "Downloading $FILE"
URL="https://s3.amazonaws.com/Minecraft.Download/versions/$VERSION/$VERSION.jar"
wget -q $URL
echo "Version Downloaded!"
rm "versions.json"
DIR="${VERSION}_src"
DISASM="${VERSION}_disasm"
mkdir "${DIR}"
mkdir "${DISASM}"
echo "Unzipping..."
unzip -q $FILE -d $DIR
echo "Disassembling..."
FILES=`ls $DIR`
for FILE in $FILES ; do
  FNAME=`echo $FILE | sed s/.class/.dis.txt/g`
  javap -p -c -constants -l $DIR/$FILE > $DISASM/$FNAME
done
