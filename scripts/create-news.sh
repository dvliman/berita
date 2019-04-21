#!/bin/bash

curl -v -XPOST http://localhost:3000/news/create-news \
  -H "Content-Type: application/json" \
  -d @create-news.json

