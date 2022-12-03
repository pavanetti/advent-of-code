module Ch01_2 where

import System.Environment
import Data.Heap hiding (take)
import qualified Data.Heap as Heap

import Ch01_1 hiding (main)

maximumN :: (Ord item) => Int -> [item] -> [item]
maximumN n = Heap.take n . maxHeapFromList

maxHeapFromList :: (Ord item) => [item] -> MaxHeap item
maxHeapFromList = fromList

sumOfMax3Calories :: String -> Int
sumOfMax3Calories = sum . maximumN 3 . elvesCaloriesFromList

main :: IO()
main = readElvesCaloriesFile >>= print . sumOfMax3Calories
