#!/bin/bash

# Set your old and new info
OLD_EMAIL="hoseinsadonasl@gmail.com"
CORRECT_NAME="hShamkhani"
CORRECT_EMAIL="shamkhanihossein93@gmail.com"

# The directory where all your projects are (change this!)
BASE_DIR="$HOME//home/hotaku/Downloads/gitscript.sh"  # Change this to the path where your repos are located

# Loop through all Git repos in the base directory
find "$BASE_DIR" -type d -name ".git" | while read gitdir; do
  repo=$(dirname "$gitdir")
  echo "Processing repo: $repo"
  cd "$repo" || continue

  git filter-branch --env-filter "
  if [ \"\$GIT_COMMITTER_EMAIL\" = \"$OLD_EMAIL\" ]; then
      export GIT_COMMITTER_NAME=\"$CORRECT_NAME\"
      export GIT_COMMITTER_EMAIL=\"$CORRECT_EMAIL\"
  fi
  if [ \"\$GIT_AUTHOR_EMAIL\" = \"$OLD_EMAIL\" ]; then
      export GIT_AUTHOR_NAME=\"$CORRECT_NAME\"
      export GIT_AUTHOR_EMAIL=\"$CORRECT_EMAIL\"
  fi
  " --tag-name-filter cat -- --branches --tags

  echo "Force pushing $repo to origin..."
  git push --force --all
  git push --force --tags
done
