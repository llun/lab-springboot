package dev.llun.practice;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class Card {
    String code;
    String suit;

    public Card(String value) {
        String[] parts = value.split("");
        code = parts[0];
        suit = parts[1];
    }

    public Integer value() {
        switch (code) {
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9": {
                return Integer.parseInt(code, 10);
            }
            case "T":
                return 10;
            case "J":
                return 11;
            case "Q":
                return 12;
            case "K":
                return 13;
            case "A":
                return 14;
            default:
                return -1;
        }
    }

    public String toString() {
        return String.format("%s", suit);
    }
}

public class PokerHand {
    private final List<Card> cards;
    private final HashMap<Integer, Integer> cardSet;

    PokerHand(String hand) {
        cards = Arrays.stream(hand.split(" ")).map(Card::new).collect(Collectors.toList());
        cards.sort(Comparator.comparingInt(Card::value));

        cardSet = cards.stream().mapToInt(Card::value).collect(
                HashMap::new,
                (c, e) -> {
                    if (c.containsKey(e)) c.put(e, c.get(e) + 1);
                    else c.put(e, 1);
                },
                HashMap::putAll
        );
    }

    public static void main(String[] args) {
        PokerHand hand1 = new PokerHand("5H 7C 3S 3H 3D");
        PokerHand hand2 = new PokerHand("2H 7C 3S 3H 3D");
        System.out.println(hand1.compareWith(hand2).name());
    }

    Boolean isInSequence() {
        List<Integer[]> list = cards.stream().map(Card::value).collect(
                (Supplier<ArrayList<Integer[]>>) ArrayList::new,
                (c, e) -> {
                    if (c.size() == 0) c.add(new Integer[]{e, 1});
                    else c.add(new Integer[]{e, e - c.get(c.size() - 1)[0]});
                },
                ArrayList::addAll
        );
        return list.stream().map(i -> i[1]).allMatch(i -> i == 1);
    }

    Boolean isTreeOfAKind() {
        return cardSet.values().stream().filter(i -> i == 3).count() == 1;
    }

    Boolean isStraight() {
        return this.isInSequence();
    }

    Boolean isStraightFlush() {
        return isStraight() && this.isFlush();
    }

    Boolean isFlush() {
        return this.isSameSuit();
    }

    boolean isFourOfAKind() {
        return cardSet.values().stream().filter(i -> i == 4).count() == 1;
    }

    boolean isFullHouse() {
        return cardSet.values().stream().filter(i -> i == 3).count() == 1 && cardSet.values().stream().filter(i -> i == 2).count() == 1;
    }

    boolean isSameSuit() {
        return cards.stream().map(card -> card.suit).collect(Collectors.toSet()).size() == 1;
    }

    boolean isTwoPair() {
        return cardSet.values().stream().filter(i -> i == 2).count() == 2;
    }

    boolean isOnePair() {
        return cardSet.values().stream().filter(i -> i == 2).count() == 1;
    }

    public Integer value() {
        return cards.stream().mapToInt(Card::value).sum();
    }

    public Integer getRank() {
        if (this.isStraightFlush()) return 9;
        if (this.isFourOfAKind()) return 8;
        if (this.isFullHouse()) return 7;
        if (this.isFlush()) return 6;
        if (this.isStraight()) return 5;
        if (this.isTreeOfAKind()) return 4;
        if (this.isTwoPair()) return 3;
        if (this.isOnePair()) return 2;
        return 1;
    }

    public Result compareWith(PokerHand hand) {
        if (this.getRank() > hand.getRank()) {
            return Result.WIN;
        }

        if (this.getRank() < hand.getRank()) {
            return Result.LOSS;
        }

        switch (this.getRank()) {
            case 9: {
                if (this.value() > hand.value()) return Result.WIN;
                if (this.value() < hand.value()) return Result.LOSS;
                return Result.TIE;
            }
            case 8: {
                Integer hand1Value = cardSet.keySet().stream().filter(k -> cardSet.get(k) == 4).collect(Collectors.toList()).get(0);
                Integer hand2Value = hand.cardSet.keySet().stream().filter(k -> hand.cardSet.get(k) == 4).collect(Collectors.toList()).get(0);
                if (hand1Value > hand2Value) return Result.WIN;
                if (hand1Value < hand2Value) return Result.LOSS;

                List<Integer> theRestOfHand1 = cardSet.keySet().stream().filter(k -> cardSet.get(k) != 4).collect(Collectors.toList());
                List<Integer> theRestOfHand2 = hand.cardSet.keySet().stream().filter(k -> hand.cardSet.get(k) != 4).collect(Collectors.toList());
                return this.compareHighestHandValue(theRestOfHand1, theRestOfHand2);
            }
            case 1:
            case 5:
            case 6: {
                return this.compareHighestHandValue(cards.stream().map(card -> card.value()).collect(Collectors.toList()), hand.cards.stream().map(card -> card.value()).collect(Collectors.toList()));
            }
            case 4:
            case 7: {
                Integer hand1Value = cardSet.keySet().stream().filter(k -> cardSet.get(k) == 3).collect(Collectors.toList()).get(0);
                Integer hand2Value = hand.cardSet.keySet().stream().filter(k -> hand.cardSet.get(k) == 3).collect(Collectors.toList()).get(0);

                if (hand1Value > hand2Value) return Result.WIN;
                if (hand1Value < hand2Value) return Result.LOSS;

                List<Integer> theRestOfHand1 = cardSet.keySet().stream().filter(k -> cardSet.get(k) != 3).collect(Collectors.toList());
                List<Integer> theRestOfHand2 = hand.cardSet.keySet().stream().filter(k -> hand.cardSet.get(k) != 3).collect(Collectors.toList());
                return this.compareHighestHandValue(theRestOfHand1, theRestOfHand2);
            }
            case 3: {
                List<Integer> hand1Values = cardSet.keySet().stream().filter(k -> cardSet.get(k) == 2).collect(Collectors.toList());
                hand1Values.sort(Comparator.comparingInt(o -> o));

                List<Integer> hand2Values = hand.cardSet.keySet().stream().filter(k -> hand.cardSet.get(k) == 2).collect(Collectors.toList());
                hand2Values.sort(Comparator.comparingInt(o -> o));

                Result pairResult = compareHighestHandValue(hand1Values, hand2Values);
                if (pairResult != Result.TIE) return pairResult;

                hand1Values = cardSet.keySet().stream().filter(k -> cardSet.get(k) == 1).collect(Collectors.toList());
                hand2Values = hand.cardSet.keySet().stream().filter(k -> hand.cardSet.get(k) == 1).collect(Collectors.toList());
                return compareHighestHandValue(hand1Values, hand2Values);
            }
            case 2: {
                Integer hand1Value = cardSet.keySet().stream().filter(k -> cardSet.get(k) == 2).collect(Collectors.toList()).get(0);
                Integer hand2Value = hand.cardSet.keySet().stream().filter(k -> hand.cardSet.get(k) == 2).collect(Collectors.toList()).get(0);
                if (hand1Value > hand2Value) return Result.WIN;
                if (hand1Value < hand2Value) return Result.LOSS;

                List<Integer> theRestOfHand1 = cardSet.keySet().stream().filter(k -> cardSet.get(k) != hand1Value).collect(Collectors.toList());
                theRestOfHand1.sort(Comparator.comparingInt(o -> o));

                List<Integer> theRestOfHand2 = hand.cardSet.keySet().stream().filter(k -> hand.cardSet.get(k) != hand2Value).collect(Collectors.toList());
                theRestOfHand2.sort(Comparator.comparingInt(o -> o));
                return this.compareHighestHandValue(theRestOfHand1, theRestOfHand2);
            }
        }
        return Result.TIE;
    }

    public Result compareHighestHandValue(List<Integer> cards1, List<Integer> cards2) {
        for (int i = cards1.size() - 1; i >= 0; i--) {
            if (cards1.get(i) > cards2.get(i)) return Result.WIN;
            else if (cards1.get(i) < cards2.get(i)) return Result.LOSS;
        }
        return Result.TIE;
    }

    public String toString() {
        return cards.toString();
    }

    public enum Result {TIE, WIN, LOSS}
}
