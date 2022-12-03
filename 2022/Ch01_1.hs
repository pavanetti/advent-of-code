module Ch01_1 where

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

filenameFromArgsOrDefault :: [String] -> String
filenameFromArgsOrDefault [] = "ch01_1.txt"
filenameFromArgsOrDefault xs = (xs !! 0)

readElvesCaloriesFile :: IO String
readElvesCaloriesFile =
    getArgs
    >>= (return . filenameFromArgsOrDefault)
    >>= readFile

main :: IO()
main = readElvesCaloriesFile >>= print . maximum . elvesCaloriesFromList