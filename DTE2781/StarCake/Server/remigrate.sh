#!/bin/bash
# Drop database, remove old migrations and add a new migration
dotnet-ef database drop -f -v -c ApplicationDbContext
rm -rf ./Migrations
dotnet-ef migrations add $1 --context ApplicationDbContext && dotnet-ef database update -v --context ApplicationDbContext
