# IWanit
Project iwanit


In this project the logged user with a buyer role creates an Article he wants to buy and another 
user with seller role answer offering an item.


Every Buyer or Seller must be related to a User with one to one relation -> a User can be Buyer, Seller or both.
We don't update a Buyer, we update a User
We don't update a Seller, we update a User

User don't create an Article or an Offer
Buyer -> creates an Article (ArticleService)
Seller -> creates an Offer related to one Article (OfferService)

Buyer can update an Article
Seller can update an Offer

Buyer can have multiple Articles
Seller can have multiple Offers -> one per article