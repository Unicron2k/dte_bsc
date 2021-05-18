#!/bin/bash
dotnet-ef migrations add $1 --context ApplicationDbContext && dotnet-ef database update -v --context ApplicationDbContext
