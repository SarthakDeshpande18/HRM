# import sys
# import pandas as pd
# import json
#
# try:
#     df = pd.read_csv(sys.argv[1])
# except Exception as e:
#     print(json.dumps({"error": f"Failed to read CSV: {e}"}))
#     sys.exit(1)
#
# if df.empty:
#     print(json.dumps({"error": "CSV file is empty"}))
#     sys.exit(1)
#
# # Check for required columns
# required_columns = {'questionId', 'question', 'answer'}
# if not required_columns.issubset(df.columns):
#     print(json.dumps({"error": f"CSV must contain {', '.join(required_columns)} columns"}))
#     sys.exit(1)
#
# # Extract the questions into a list of dictionaries
# qa_list = []
# for _, row in df.iterrows():
#     qa_list.append({
#         "questionId": str(row['questionId']),  # ensure it's a string
#         "question": row['question'],
#         "answer": row['answer']
#     })
#
# # Output the list as JSON
# print(json.dumps(qa_list))
#


import sys
import pandas as pd
import json

try:
    try:
        df = pd.read_csv(
            sys.argv[1],
            encoding='utf-8',
            on_bad_lines='skip',  # skip bad rows (pandas >= 1.3)
            quotechar='"'         # handles commas inside quotes
        )
    except UnicodeDecodeError:
        df = pd.read_csv(
            sys.argv[1],
            encoding='ISO-8859-1',
            on_bad_lines='skip',
            quotechar='"'
        )
except Exception as e:
    print(json.dumps({"error": f"Failed to read CSV: {e}"}))
    sys.exit(1)

if df.empty:
    print(json.dumps({"error": "CSV file is empty"}))
    sys.exit(1)

# Check for required columns
required_columns = {'questionId', 'question', 'answer'}
if not required_columns.issubset(df.columns):
    print(json.dumps({"error": f"CSV must contain {', '.join(required_columns)} columns"}))
    sys.exit(1)

# Extract the questions into a list of dictionaries
qa_list = []
for _, row in df.iterrows():
    qa_list.append({
        "questionId": str(row['questionId']),
        "question": row['question'],
        "answer": row['answer']
    })

# Output the list as JSON
print(json.dumps(qa_list))
