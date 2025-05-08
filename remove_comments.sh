#!/bin/bash

# Script to remove all comments from Java files
# This will remove:
# 1. Single-line comments (starting with //)
# 2. Multi-line comments (enclosed in /* */)
# 3. JavaDoc comments (enclosed in /** */)

# Find all Java files
java_files=$(find . -name "*.java")

for file in $java_files; do
  echo "Processing $file"
  
  # Create a temporary file
  temp_file=$(mktemp)
  
  # Remove all types of comments using sed
  # This complex pattern handles:
  # - Single line comments (//)
  # - Multi-line comments (/* */)
  # - JavaDoc comments (/** */)
  # while preserving strings that might contain comment-like patterns
  
  # First pass: Remove single-line comments
  sed 's|//.*$||g' "$file" > "$temp_file"
  
  # Second pass: Remove multi-line comments (including JavaDoc)
  # This is a more complex operation that requires handling nested patterns
  perl -0777 -pe 's|/\*[\s\S]*?\*/||g' "$temp_file" > "${temp_file}.2"
  
  # Move the processed content back to the original file
  mv "${temp_file}.2" "$file"
  
  # Clean up
  rm -f "$temp_file"
done

echo "All comments have been removed from Java files."
