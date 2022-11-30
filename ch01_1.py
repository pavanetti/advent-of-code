with open("ch01_1.txt") as reading_file:
    count, previous = 0, None
    for current in reading_file:
        if previous and int(current) > int(previous):
            count += 1
        previous = current
    print(count)