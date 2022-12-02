import System.Environment

elvesCaloriesFromList :: String -> [Int]
elvesCaloriesFromList = map (sum . mapToInt) . splitOnBlankLine . lines
    where
        mapToInt = map (\s -> read s :: Int)

splitOnBlankLine :: [String] -> [[String]]
splitOnBlankLine ss = collect ss [] []
    where
        collect ("":ss) chunk acc = collect ss [] (chunk:acc)
        collect (s:ss) chunk acc = collect ss (s:chunk) acc
        collect [] chunk acc = (chunk:acc)


main :: IO()
main = getArgs >>= filenameFromArgsOrDefault >>= readFile >>= printMaximum
    where
        filenameFromArgsOrDefault [] = return "ch01_1.txt"
        filenameFromArgsOrDefault xs = return (xs !! 0)
        printMaximum = print . maximum . elvesCaloriesFromList