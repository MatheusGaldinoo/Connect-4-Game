# Connect Four - Terminal Game - Matheus Galdino

def startGame():
    DEFAULT_GRID = [[0, 0, 0, 0, 0, 0, 0],
                    [0, 0, 0, 0, 0, 0, 0],
                    [0, 0, 0, 0, 0, 0, 0],
                    [0, 0, 0, 0, 0, 0, 0],
                    [0, 0, 0, 0, 0, 0, 0],
                    [0, 0, 0, 0, 0, 0, 0]
                    ]

    victories = [0, 0]

    print(f"| ScoreBoard: {victories[0]} X {victories[1]}  |")
    print("----------------------\n")

    continueGame = True
    player = 1

    while continueGame:
        victories = startRound(DEFAULT_GRID, victories, player)

        print(f"ScoreBoard: {victories[0]} X {victories[1]}")

        if input("Do you want to keep playing? ('y' to continue / any other to quit): ") != "y":
            continueGame = False
            print("The End!")
        print()

        player = 3 - player

def startRound(DEFAULT_GRID, victories, player):
    grid = [row[:] for row in DEFAULT_GRID]

    while True:
        printGrid(grid)

        try:
            move = int(input(f"Which column do you want to put {color(player)}? (1-7) or 0 for resignation: ")) - 1
            if move < -1 or move > 6 or (move >= 0 and not isColValid(grid, move)):
                raise ValueError
        except ValueError:
            print("INVALID MOVE!\n")
            continue
        print()

        if move == -1:
            return addVictory(victories, player + 1)

        lin = updateGrid(grid, move, player)

        if lin >= 0:
            if hasWinner(grid, lin, move, player):
                printGrid(grid)
                return addVictory(victories, player)
            elif isFull(grid):
                printGrid(grid)
                print("Draw!")
                break

        player = 3 - player

def printGrid(grid):
    for lin in range(len(grid)):
        for col in range(len(grid[lin])):
            if grid[lin][col] == 1:
                print("|🟡", end="")
            elif grid[lin][col] == 2:
                print("|🔴", end="")
            else:
                print("|  ", end="")
        print("|")
    print("----------------------")
    print("| 1| 2| 3| 4| 5| 6| 7|\n")

def isColValid(grid, col):
    return 0 <= col <= 6 and grid[0][col] == 0

def updateGrid(grid, col, player):
    for lin in range(len(grid) - 1, -1, -1):
        if grid[lin][col] == 0:
            grid[lin][col] = player
            return lin
    return -1

def hasWinner(grid, lin, col, player):
    if checkLine(grid, lin, col, player) >= 4:
        return True
    elif checkColumn(grid, lin, col, player) >= 4:
        return True
    elif checkDiagonal1(grid, lin, col, player) >= 4:
        return True
    elif checkDiagonal2(grid, lin, col, player) >= 4:
        return True

def checkLine(grid, lin, col, player):
    count = 0

    # Check left
    aux = col
    while aux >= 0 and grid[lin][aux] == player:
        count += 1
        aux -= 1

    # Check right
    aux = col + 1
    while aux < 7 and grid[lin][aux] == player:
        count += 1
        aux += 1

    return count

def checkColumn(grid, lin, col, player):
    count = 0
    while lin < 6 and grid[lin][col] == player:
        count += 1
        lin += 1

    return count

def checkDiagonal1(grid, lin, col, player):
    count = 0

    # Check top-left
    aux = 0
    while lin - aux >= 0 and col - aux >= 0 and grid[lin - aux][col - aux] == player:
        count += 1
        aux += 1

    # Check bottom-right
    aux = 1
    while lin + aux < 6 and col + aux < 7 and grid[lin + aux][col + aux] == player:
        count += 1
        aux += 1

    return count

def checkDiagonal2(grid, lin, col, player):
    count = 0

    # Check bottom-left
    aux = 0
    while lin + aux < 6 and col - aux >= 0 and grid[lin + aux][col - aux] == player:
        count += 1
        aux += 1

    # Check top-right
    aux = 1
    while lin - aux >= 0 and col + aux < 7 and grid[lin - aux][col + aux] == player:
        count += 1
        aux += 1

    return count

def addVictory(victories, player):
    print(f"Player {player} wins!")
    victories[player - 1] += 1
    return victories

def isFull(grid):
    for lin in range(6):
        for col in range(7):
            if grid[lin][col] == 0:
                return False
    return True

def color(player):
    if player == 1:
        return "🟡"
    else:
        return "🔴"

if __name__ == '__main__':
    startGame()
