#!/bin/sh
grep -h -e 'http[a-zA-Z]*://[a-zA-Z0-9./?]*' *_disasm/*.txt > "found_services"
