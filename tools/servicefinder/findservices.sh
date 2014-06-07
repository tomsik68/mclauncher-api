#!/bin/sh
SRC_DIR=`ls | grep disasm`
grep -h -e 'http[a-zA-Z]*://[a-zA-Z0-9./?]*' $SRC_DIR/*.txt > "found_services"
