import os

source_file = r"app\src\main\java\com\example\ui\screens\MainAgentView.kt"
out_dir = r"app\src\main\java\com\example\ui\screens\components"
main_out = r"app\src\main\java\com\example\ui\screens\MainAgentView.kt"

with open(source_file, "r", encoding="utf-8") as f:
    lines = f.readlines()

# Extract header (package + imports)
header = []
idx = 0
while idx < len(lines):
    line = lines[idx]
    if line.startswith("@Composable") or line.startswith("fun ") or line.startswith("private fun "):
        break
    header.append(line)
    idx += 1

# Change package for components
component_header = "".join(header).replace("package com.example.ui.screens", "package com.example.ui.screens.components\n\nimport com.example.ui.screens.*")
main_header = "".join(header) + "\nimport com.example.ui.screens.components.*\n"

# Parse functions
functions = {}
current_func = None
func_lines = []
brace_count = 0
in_func = False

for i in range(idx, len(lines)):
    line = lines[i]
    
    if (line.startswith("@Composable") and brace_count == 0) or (line.startswith("fun ") and brace_count == 0) or (line.startswith("private fun ") and brace_count == 0):
        if current_func:
            functions[current_func] = "".join(func_lines)
            
        if line.startswith("@Composable"):
            # Next line usually has the name
            name_line = lines[i+1]
            if name_line.startswith("fun "):
                current_func = name_line.split("fun ")[1].split("(")[0].strip()
            else:
                current_func = "unknown_" + str(i)
        else:
            current_func = line.split("fun ")[1].split("(")[0].strip()
            
        func_lines = []
        in_func = True

    if in_func:
        func_lines.append(line)
        brace_count += line.count("{") - line.count("}")
        
    if in_func and brace_count == 0 and len(func_lines) > 1:
        # Function ended
        functions[current_func] = "".join(func_lines)
        current_func = None
        in_func = False
        func_lines = []

# If EOF reached and still in func
if current_func and func_lines:
    functions[current_func] = "".join(func_lines)

# Write out the files
chat_funcs = ["AgentChatTab", "ChatMessageBubble", "ProposedActionBlock", "TypingIndicatorBubble", "formatTime"]
settings_funcs = ["AgentSettingsTab", "SpecialPermissionItemRow", "PermissionItemRow"]
calendar_funcs = ["SystemCalendarTab", "CalendarEventCard"]
bg_funcs = ["AnimatedLiquidBackground"]
main_funcs = ["MainAgentView"]

def write_file(filename, funcs, is_component=True):
    with open(filename, "w", encoding="utf-8") as f:
        f.write(component_header if is_component else main_header)
        for fn in funcs:
            if fn in functions:
                f.write(functions[fn] + "\n")

write_file(os.path.join(out_dir, "ChatScreen.kt"), chat_funcs)
write_file(os.path.join(out_dir, "SettingsScreen.kt"), settings_funcs)
write_file(os.path.join(out_dir, "CalendarScreen.kt"), calendar_funcs)
write_file(os.path.join(out_dir, "Backgrounds.kt"), bg_funcs)
write_file(main_out, main_funcs, is_component=False)

print("Split completed successfully!")
