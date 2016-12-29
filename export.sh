#!/usr/bin/env bash
if [ ! -d "export" ]; then
  mkdir export
fi
cd export
rm -rf HeartCool *.tar *.gz
git clone /media/wd/WD-GitHub/Android/HeartCool.git
cd HeartCool
rm -rf .git .idea doc key export.sh .gitignore
cd ..
tar -cvf HeartCool.tar HeartCool
rm -rf HeartCool
gzip HeartCool.tar
