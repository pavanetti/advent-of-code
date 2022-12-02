from ch01_1 import solve

window_size = 3

with open("ch01_2.txt", "w") as moving_window_file:
    with open("ch01_1.txt") as reading_file:
        reading_count = 0
        window = [None] * window_size
        for reading in reading_file:
            window.pop(0)
            window.append(int(reading))
            reading_count += 1
            if reading_count >= window_size:
                moving_window_file.write(str(sum(window)) + '\n')

solve("ch01_2.txt")