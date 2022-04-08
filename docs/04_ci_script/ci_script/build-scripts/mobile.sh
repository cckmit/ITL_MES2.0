#!/bin/bash
echo $CI_PROJECT_PATH_SLUG
npm install || exit 1 && \
   npm run build || exit 1 && \
   mkdir -p publish/mobiledist && \
   cp -r dist/*  publish/mobiledist/ || exit 1
echo "build sucessful!"
