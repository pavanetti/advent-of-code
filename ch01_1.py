def solve(file):
    with open(file) as reading_file:
        count, previous = 0, None
        for current in reading_file:
            if previous and int(current) > int(previous):
                count += 1
            previous = current
        print(count)

if __name__ == "__main__":
    solve("ch01_1.txt")
