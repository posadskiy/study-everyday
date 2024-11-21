The Fast and Slow pointer approach, also known as the Hare & Tortoise algorithm, is a pointer algorithm that uses two
pointers which move through the array (or sequence/linked list) at different speeds. This approach is quite useful when
dealing with cyclic linked lists or arrays.

By moving at different speeds (say, in a cyclic linked list, and usually 1x and 2x speed), the algorithm proves that the
two pointers are bound to meet. The fast pointer should catch the slow pointer once both the pointers are in a cyclic
loop.

For removing cycled link, needs to stop on element where fast iterator catches slow iterator. Then, one of the iterator
should be assigned to the head of the list and both iterators started with the same speed (1x). Element where they meet
is the element with two links on it. Needs to continue first iterator up to it's `next` will point on second iterator,
then remove `next` link from first iterator.
