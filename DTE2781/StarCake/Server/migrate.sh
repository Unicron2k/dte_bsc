#!/bin/bash
# Add a new migration
dotnet-ef migrations add $1 --context ApplicationDbContext && dotnet-ef database update -v --context ApplicationDbContext
