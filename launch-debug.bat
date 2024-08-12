@echo DEBUG

@echo off
for /f "tokens=2 delims=:." %%x in ('chcp') do set cp=%%x
chcp 1252>nul

java --module-path "javafx/lib" --add-modules javafx.controls,javafx.fxml -jar agence-voyage.jar

chcp %cp%>nul