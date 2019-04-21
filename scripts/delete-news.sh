#!/bin/bash

curl -v -XPOST http://localhost:3000/news/delete-news \
    -H "Content-Type: application/json" \
    -d @delete-news.json