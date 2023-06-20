@echo off

if exist src\grmatica\errores_Sem.txt  (
    del src\grmatica\errores_Sem.txt 
    
) 
if exist src\grmatica\erroresSin.txt (
    del src\grmatica\erroresSin.txt 
    
) 
if exist src\grmatica\errores_tok.txt  (
    del src\grmatica\errores_tok.txt 
    
) 
if exist src\grmatica\prueba_token.txt  (
    del src\grmatica\prueba_token.txt 
    
) 
if exist src\grmatica\TSimbolos.txt (
    del src\grmatica\TSimbolos.txt
    
) 

if exist src\grmatica\parse.txt (
    del src\grmatica\parse.txt
    
) 
pause

javac src\code\Analizador.java
java src.code.Analizador 
