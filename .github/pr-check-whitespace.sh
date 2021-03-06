#! /bin/bash

echo ""
echo "Checking for white space errors"
echo ""

if [ -z "$TRAVIS_BRANCH" ]; then
    echo "TRAVIS_BRANCH is undefined, skipping white-space check"
    exit 0
fi

ROOTDIR=$(git rev-parse --show-toplevel)
cd $ROOTDIR

# Check all files changed as part of this pull request
# FIXME: maybe remove the -v option at some point
bash .github/pr-list-files.sh | bash tools/scripts/checkWhiteSpace -S -x -v
