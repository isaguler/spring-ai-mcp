package com.isaguler.spring_ai_mcp.service;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallback;
import org.springframework.stereotype.Service;

@Service
public class WebSearchService {

    private final Logger log = LoggerFactory.getLogger(WebSearchService.class);

    private final ChatClient.Builder chatClientBuilder;

    public WebSearchService(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
    }

    public String searchFromWeb(String message) {
        var stdioParams = ServerParameters.builder("C:\\NodeJS\\npx.cmd")
                .args("-y", "@modelcontextprotocol/server-brave-search")
                .addEnvVar("BRAVE_API_KEY", System.getenv("BRAVE_API_KEY"))
                .build();

        var mcpClient = McpClient.sync(new StdioClientTransport(stdioParams)).build();

        var init = mcpClient.initialize();

        log.info("MCP client initialized: {}", init);

        var chatClient = chatClientBuilder
                .defaultTools(mcpClient.listTools(null)
                        .tools()
                        .stream()
                        .map(tool -> new SyncMcpToolCallback(mcpClient, tool))
                        .toArray(SyncMcpToolCallback[]::new))
                .build();

        var response = chatClient.prompt(message).call().content();

        log.info("Response from MCP: {}", response);

        return response;
    }

}
