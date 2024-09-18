package local.linux.tfy_curs.util;

import local.linux.tfy_curs.model.Token;

import java.util.List;

import java.util.List;

import java.util.List;

import java.util.List;

public class Parser {
    private List<Token> tokens;
    private Token token;
    private int numberToken = -1;
    private boolean check = true;
    private String result = "";

    public String getResult() {
        return result;
    }

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private void next() {
        if (numberToken + 1 < tokens.size()) {
            numberToken++;
            token = tokens.get(numberToken);
        }
    }

    public boolean run() {
        next();
        program();
        return check;
    }

    private void program() {
        if (token.getType() == Token.TokenType.VAR) {
            next();
            listDescription();
            if (token.getType() == Token.TokenType.BEGIN) {
                next();
                listOperation();
                if (token.getType() == Token.TokenType.END) {
                    next();
                    if (token.getType() != Token.TokenType.POINT) {
                        check = false;
                    }
                } else {
                    check = false;
                }
            } else {
                check = false;
            }
        } else {
            check = false;
        }
    }

    // Список описаний переменных
    private void listDescription() {
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            description();
            if (token.getType() == Token.TokenType.SEMICOLON) {
                next();
                altDescription();
            }
        } else {
            check = false;
        }
    }

    // Описание переменной
    private void description() {
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            listVariable();
            if (token.getType() == Token.TokenType.COLON) {
                next();
                type();
            }
        } else {
            check = false;
        }
    }

    // Список переменных
    private void listVariable() {
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            next();
            altListVariable();
        } else {
            check = false;
        }
    }

    // Альтернативный список переменных
    private void altListVariable() {
        if (token.getType() == Token.TokenType.COMMA) {
            next();
            listVariable();
        } else if (token.getType() != Token.TokenType.COLON) {
            check = false;
        }
    }

    // Альтернативное описание
    private void altDescription() {
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            listDescription();
        } else if (token.getType() != Token.TokenType.BEGIN) {
            check = false;
        }
    }

    // Тип переменной
    private void type() {
        if (token.getType() == Token.TokenType.INTEGER || token.getType() == Token.TokenType.REAL || token.getType() == Token.TokenType.DOUBLE) {
            next();
        } else {
            check = false;
        }
    }

    // Список операций
    private void listOperation() {
        while (token.getType() == Token.TokenType.IDENTIFIER || token.getType() == Token.TokenType.IF) {
            operation();
            if (token.getType() == Token.TokenType.SEMICOLON) {
                next();
            } else if (token.getType() != Token.TokenType.END && token.getType() != Token.TokenType.ELSE) {
                check = false;
                return;
            }
        }
    }

    // Операция
    private void operation() {
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            appropriation();
        } else if (token.getType() == Token.TokenType.IF) {
            comparison();
        } else {
            check = false;
        }
    }

    // Присваивание
    private void appropriation() {
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            next();
            if (token.getType() == Token.TokenType.ASSIGNMENT) {
                next();
                operand();
            } else {
                check = false;
            }
        } else {
            check = false;
        }
    }

    // Условный оператор
    private void comparison() {
        if (token.getType() == Token.TokenType.IF) {
            next();
            logicalExpression();
            if (token.getType() == Token.TokenType.THEN) {
                next();
                if (token.getType() != Token.TokenType.ELSE && token.getType() != Token.TokenType.END) {
                    blockOperation();
                }
                altBlockOperation();
            } else {
                check = false;
            }
        } else {
            check = false;
        }
    }

    // Альтернативная обработка блока
    private void altBlockOperation() {
        if (token.getType() == Token.TokenType.ELSE) {
            next();
            blockOperation();
        }
    }

    // Блок операций
    private void blockOperation() {
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            appropriation();
        } else if (token.getType() == Token.TokenType.BEGIN) {
            next();
            listOperation();
            if (token.getType() == Token.TokenType.END) {
                next();
            } else {
                check = false;
            }
        }
    }

    // Операнд
    private void operand() {
        if (token.getType() == Token.TokenType.IDENTIFIER || token.getType() == Token.TokenType.NUMBER) {
            next();
        } else {
            check = false;
        }
    }

    // Логическое выражение
    private void logicalExpression() {
        operand();
        compare();
        operand();
    }

    // Сравнение
    private void compare() {
        if (token.getType() == Token.TokenType.MORE || token.getType() == Token.TokenType.LESS) {
            next();
        } else {
            check = false;
        }
    }
}
