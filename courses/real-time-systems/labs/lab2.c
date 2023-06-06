#include<stdio.h>
#include<string.h>
#include<stdlib.h>

/*
	Each element in a list is a person, with an attribute "name"
	And an emergency contact, who can be anyone else on the list
	(including themselves)

	"next" is standard linked list field, to connect each element to the next one
	(so we just need to keep a reference to the first element)
*/

struct list_node
{
	char *name;
	struct list_node *contact;
	struct list_node *next;	
};


/*
	This function creates an original linked list
	This list is just for testing purposes; you're encouraged to create new ones to try it out
*/
struct list_node *create_list()
{
	struct list_node *nodes[10];
	int i;

	//Allocates memory for all nodes
	for(i=0; i<10; i++)
	{
		nodes[i] = (struct list_node *)malloc(sizeof(struct list_node));
	}

	//links all the "next" fields
	for(i=0; i<9; i++)
	{
		nodes[i]->next = nodes[i+1];
	}
	nodes[9]->next = NULL;

	nodes[0]->name = (char *)malloc(strlen("John")+1);
	strcpy(nodes[0]->name,"John");
	nodes[0]->contact = nodes[1];

	nodes[1]->name = (char *)malloc(strlen("Mary")+1);
	strcpy(nodes[1]->name,"Mary");
	nodes[1]->contact = nodes[9];

	nodes[2]->name = (char *)malloc(strlen("Mohamed")+1);
	strcpy(nodes[2]->name,"Mohamed");
	nodes[2]->contact = nodes[0];

	nodes[3]->name = (char *)malloc(strlen("Liza")+1);
	strcpy(nodes[3]->name,"Liza");
	nodes[3]->contact = nodes[4];

	nodes[4]->name = (char *)malloc(strlen("Osenya")+1);
	strcpy(nodes[4]->name,"Osenya");
	nodes[4]->contact = nodes[4];

	nodes[5]->name = (char *)malloc(strlen("Peter")+1);
	strcpy(nodes[5]->name,"Peter");
	nodes[5]->contact = nodes[7];

	nodes[6]->name = (char *)malloc(strlen("Mahala")+1);
	strcpy(nodes[6]->name,"Mahala");
	nodes[6]->contact = nodes[2];

	nodes[7]->name = (char *)malloc(strlen("Rita")+1);
	strcpy(nodes[7]->name,"Rita");
	nodes[7]->contact = nodes[4];

	nodes[8]->name = (char *)malloc(strlen("Jacques")+1);
	strcpy(nodes[8]->name,"Jacques");
	nodes[8]->contact = nodes[8];

	nodes[9]->name = (char *)malloc(strlen("Paul")+1);
	strcpy(nodes[9]->name,"Paul");
	nodes[9]->contact = nodes[1];


	return nodes[0];
}

/*
	Prints the list and connections
	Carefully coded so you will have segmentation faults if your copy is not perfect
*/
void print_list(struct list_node *l)
{
	int i=0;
	while(l != NULL)
	{
		printf("Entry %d is %s, contact is %s\n",i,l->name,l->contact->name);
		i++;
		l = l->next;
	}
	printf("\n");
}


/*
	This is what you have to implement
	Remember this should work for any possible linked list implemented using the structure above,
	not just for the particular example given here
*/
struct list_node *copy_list(struct list_node *original)
{
    struct list_node *curr = original;    
    struct list_node *copyHead = NULL;
    struct list_node* copyRest = NULL;
    

    while(curr != NULL)
    {
        if (copyHead == NULL){ //For first node copied into new list

            //Creates memory for first node of the list copy
            copyHead = (struct list_node*)malloc(sizeof(struct list_node)); 


            //Creates memory for the name of the first node of the list copy
            copyHead->name = (char *)malloc(strlen(curr->name)+1);
            strcpy(copyHead->name,curr->name);  


            //Creates memory for the contact of the first node of the list copy
            copyHead->contact = (struct list_node *)malloc(sizeof(struct list_node));
            copyHead->contact->name = (char *)malloc(strlen(curr->contact->name)+1);
            strcpy(copyHead->contact->name, curr->contact->name); 
            
            copyHead->next = curr->next;
            copyRest = copyHead;
        }
        else {

            //Creates memory and sets fields for subsequent nodes of the list copy
            copyRest->next = (struct list_node*)malloc(sizeof(struct list_node)); 
            copyRest = copyRest->next;
            
	    //Creates memory for the name of subsequent nodes of the list copy
            copyRest->name = (char *)malloc(strlen(curr->name)+1);
            strcpy(copyRest->name,curr->name); 

	    //Creates memory for the contact of subsequent nodes of the list copy            
            copyRest->contact = (struct list_node *)malloc(sizeof(struct list_node));
            copyRest->contact->name = (char *)malloc(strlen(curr->contact->name)+1);
            strcpy(copyRest->contact->name, curr->contact->name); 
            
        }
        curr = curr->next;       
    }
    return copyHead;
}


/*
	Deletes a list
*/
struct list_node *delete_list(struct list_node *l)
{
	struct list_node *tmp;
	while(l != NULL)
	{
		tmp = l->next;

		free(l->name);
		l->name = NULL;
		l->contact = NULL;
		l->next = NULL;

		free(l);

		l=tmp; 
	}
	return NULL;
}

int main()
{
	struct list_node *original = NULL;
	struct list_node *copy = NULL;

	//create linked list
	original = create_list();

	//print linked list
	printf("Original:\n");
	print_list(original);

	//copy linked list
	copy = copy_list(original);

	//delete original linked list
	original = delete_list(original);

	//print copy
	printf("Copy:\n");
	//should print exactly the same thing as the original
	print_list(copy);
}