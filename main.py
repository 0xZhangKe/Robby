import random
import time


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
    room_x_len = len(room)
    room_y_len = len(room)
    for _ in range(0, 200):
        rules = get_rule(x, y, item, room)
        if rules == 0:
            x -= 1
            if x < 0:
                x = 0
                score -= 5
        elif rules == 1:
            x += 1
            if x >= room_x_len:
                x = room_x_len - 1
                score -= 5
        elif rules == 2:
            y -= 1
            if y < 0:
                y = 0
                score -= 5
        elif rules == 3:
            y += 1
            if y >= room_y_len:
                y = room_y_len - 1
                score -= 5
        elif rules == 4:
            x, y = random_dir(x, y)
            if x < 0 or y < 0 or x >= room_x_len or y >= room_y_len:
                score -= 5
                if x < 0:
                    x = 0
                if x >= room_x_len:
                    x = room_x_len - 1
                if y < 0:
                    y = 0
                if y >= room_y_len:
                    y = room_y_len - 1
        elif rules == 5:
            if room[x][y]:
                score += 10
            room[x][y] = False
    return score


def get_rule(x: int, y: int, item: [], room: [[bool]]):
    room_x_len = len(room)
    room_y_len = len(room)
    # print(x, y)
    top = 0
    if x - 1 < 0:
        top = 2
    elif room[x - 1][y]:
        top = 1
    bottom = 0
    if x + 1 >= room_x_len:
        bottom = 2
    elif room[x + 1][y]:
        bottom = 1
    left = 0
    if y - 1 < 0:
        left = 2
    elif room[x][y - 1]:
        left = 1
    right = 0
    if y + 1 >= room_y_len:
        right = 2
    elif room[x][y + 1]:
        right = 1
    current = 0
    if x < 0 or y < 0 or x >= room_x_len or y >= room_y_len:
        current == 2
    elif room[x][y]:
        current = 1
    value = str(top) + str(bottom) + str(left) + str(right) + str(current)
    return item[int(value, 3)]


def random_dir(x: int, y: int):
    ran = random.randint(0, 4)
    if ran == 0:
        return x - 1, y
    elif ran == 1:
        return x, y - 1
    elif ran == 2:
        return x + 1, y
    else:
        return x, y + 1


if __name__ == '__main__':
    start_ts = time.time()
    initEveCount = 200
    cleanCount = 200
    eve = []
    for num in range(0, initEveCount):
        eve.append(generate_item())
    adaptability = []
    for e in eve:
        score = 0
        for i in range(0, cleanCount):
            score += rate_item(e, generate_room(0.3))
        average = int(score / cleanCount)
        adaptability.append(average)
    print(adaptability)
    min_ada = 100000
    for i in adaptability:
        min_ada = min(i, min_ada)
    min_ada = 1 + abs(min_ada)
    for i in range(0, len(adaptability)):
        adaptability[i] += min_ada
    print(adaptability)
    print("spend:" + str(time.time() - start_ts) + "(s)")
