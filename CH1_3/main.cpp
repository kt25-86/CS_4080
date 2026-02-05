#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct sNode
{
    struct sNode *previous;
    struct sNode *next;
    char *value;

}Node;

void insert(Node **list, Node *previous, const char *value)
{
    Node *node = (Node *)malloc(sizeof(Node));
    node->value = (char*)malloc(strlen(value) + 1);
    strcpy(node->value, value);

    if (previous == NULL)
    {
        if (*list != NULL)
        {
            (*list)->previous = node;
        }
        node->previous = NULL;
        node->next = *list;
        *list = node;
    }else
    {
        node->next = previous->next;
        if (node->next != NULL)
        {
            node->next->previous = node;
        }
        previous->next = node;
        node->previous = previous;
    }
}

Node* find(Node *list, const char *value)
{
    while (list != NULL)
    {
        if (strcmp(value, (list)->value) == 0)
        {
            return list;
        }
        list = list->next;
    }

    return NULL;
}

void del(Node** list, Node* node){
    if (node->previous != NULL) node->previous->next = node->next;
    if (node->previous != NULL) node->next->previous = node->previous;

    if (*list == node)
    {
        *list = node->next;
    }

    free(node->value);
    free(node);
}

void dump(Node *list)
{
    while (list != NULL)
    {
        printf("%p [previous %p next %p] %s\n", list, list->previous, list->next, list->value);
        list = list->next;
    }
}

int main(int argc, const char* argv[]){
    printf("Hello, world!\n");

    Node* list = NULL;
    insert(&list, NULL, "four");
    insert(&list, NULL, "one");
    insert(&list, find(list, "one"), "two");
    insert(&list, find(list, "two"), "three");

    dump(list);
    printf("--delete three--\n");
    del(&list, find(list, "three"));
    dump(list);

    printf("--delete one--\n");
    del(&list, find(list, "one"));
    dump(list);

}
