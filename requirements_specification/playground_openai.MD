Example request to OpenAI:

``curl https://api.openai.com/v1/chat/completions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $OPENAI_API_KEY" \
  -d '{
  "model": "gpt-3.5-turbo",
  "messages": [
    {
      "role": "system",
      "content": "You are extracting keywords from user queries passed to a chat bot for a university. You give the relevant keywords, separated by comma and nothing else. Your first word will be ONLY one of those: WHERE,HOW,WHEN,WHAT based on what the question wants"
    },
    {
      "role": "user",
      "content": "when does the campus at prittwitzstrasse close on sunday?"
    },
    {
      "role": "assistant",
      "content": "WHEN, campus, prittwitzstrasse, close, Sunday"
    }
  ],
  "temperature": 0.24,
  "max_tokens": 256,
  "top_p": 1,
  "frequency_penalty": 0,
  "presence_penalty": 0
}``

