import random


def generate_item():
    item = []
    for _ in range(0, 243):
        item.append(random.randint(0, 6))
    return item


def generate_room(factor: float):
    room = []
    factor = factor * 100
    for i in range(0, 10):
        line = []
        for j in range(0, 10):
            line.append(random.randint(0, 100) < factor)
        room.append(line)
    return room


def rate_item(item: [], room: [[bool]]):
    score = 0
    x = 5
    y = 5
    roomXLen = len(room)
    roomYLen = len(room)
    for _ in range(0, 200):
        top = 0
        if (x - 1 < 0):
            top = 2
        elif (room[x - 1][y]):
            top = 1
        bottom = 0
        if (x + 1 > roomXLen):
            bottom = 2
        elif (room[x + 1][y]):
            bottom = 1
        left = 0
        if (y - 1 < 0):
            left = 2
        elif (room[x][y - 1]):
            left = 1
        right = 0
        if (y + 1 > roomYLen):
            right = 2
        elif (room[x][y + 1]):
            right = 1
        current = 0
        if (x < 0 or y < 0 or x > roomYLen or y > roomYLen):
            current == 2
        elif (room[x][y]):
            current = 1
        number = str(top) + str(bottom) + str(left) + str(right) + str(current)
        rules = item[int(number, 3)]


if __name__ == '__main__':
    number = "00002"
    print(number)
    print(int(number, 3))
    # print(generate_room(0.3))
    # ava = []
    # for num in range(0, 200):
    #     ava.append(generate_item())
    # print(ava)
